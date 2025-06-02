package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.Review;
import com.example.labourApp.Entity.Bookings;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.BookingRepository;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.LabourService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Data
@Service
public class LabourServiceImpl implements LabourService {


    private final ExecutorService executorService = Executors.newFixedThreadPool(10);


    @Autowired
    private LabourRepository labourRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Async
    public CompletableFuture<ResponseDTO> registerLabour(LabourDTO details) {

        String mobileNo = details.getLabourMobileNo();

        boolean isExists = labourRepository.existsByLabourMobileNo(mobileNo);

        if(isExists){
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Mobile Number already registered, try different !!"));
        }

        Labour labour = mapDtoToEntity(details);
        labourRepository.save(labour);
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Registered !!"));
    }

    @Async
    public  CompletableFuture<ResponseDTO> labourLogin(String mobileNumber){
        Optional<Labour> l = labourRepository.findByLabourMobileNo(mobileNumber);
        if(l.isPresent()){
           return CompletableFuture.completedFuture(new ResponseDTO(l.get(), false, "Successfully Fetched labour !!"));
        }

        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Didn't find any labour with this mobile number !!"));
    }



    @Async
    public CompletableFuture<PaginationResponseDTO> findLabourByCategory(PaginationRequestDTO paginationRequestDTO, String category) {

        Integer pageNumber = paginationRequestDTO.getPageNumber();
        Integer pageSize = paginationRequestDTO.getPageSize();
        String sortBy = paginationRequestDTO.getSortBy();
        String sortOrder = paginationRequestDTO.getSortOrder();

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<Labour> labourListPage = labourRepository.findByLabourSkill(category, p);

        List<Labour> labourList = labourListPage.getContent();

        List<LabourDTO> dtoList = labourList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new PaginationResponseDTO(dtoList, labourListPage.getNumber(), labourListPage.getSize(), labourListPage.getTotalElements(), labourListPage.getTotalPages(), labourListPage.isLast()));
    }

    @Async
    public CompletableFuture<PaginationResponseDTO> findAllLabours(PaginationRequestDTO paginationRequestDTO) {

        Integer pageNumber = paginationRequestDTO.getPageNumber();
        Integer pageSize = paginationRequestDTO.getPageSize();
        String sortBy = paginationRequestDTO.getSortBy();
        String sortOrder = paginationRequestDTO.getSortOrder();

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<Labour> pageLabour = this.labourRepository.findAll(p);

        List<Labour> labourList = pageLabour.getContent();

        List<LabourDTO> dtoList = labourList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new PaginationResponseDTO(dtoList, pageLabour.getNumber(), pageLabour.getSize(),
                pageLabour.getTotalElements(), pageLabour.getTotalPages(), pageLabour.isLast()));
    }

    @Async
    public CompletableFuture<ResponseDTO> findLabour(Integer labourId) {
        Optional<Labour> labour = labourRepository.findById(labourId);
        if (labour.isPresent()) {
            LabourDTO dto = mapEntityToDto(labour.get());
            return CompletableFuture.completedFuture(new ResponseDTO(dto, false, "Fetched successfully"));
        }
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Unable to fetch !!"));
    }


    @Async
    public CompletableFuture<ResponseDTO> rateLabour(Map<String, Object> reqBody) {
        Integer userId = (Integer) reqBody.get("userId");
        Integer labourId = (Integer) reqBody.get("labourId");
        Integer bookingId = (Integer) reqBody.get("bookingId");
        double rating = Double.parseDouble((reqBody.get("labourRating")).toString());
        String review = (String) reqBody.get("review");

        return CompletableFuture.supplyAsync(() ->
                        labourRepository.findById(labourId), executorService)
                .thenCompose(optionalLabour -> {
                    if (optionalLabour.isEmpty()) {
                        return CompletableFuture.completedFuture(
                                new ResponseDTO(null, true, "Labour not found")
                        );
                    }

                    return bookingRepository.findByBookingIdAndUserIdAndLabourId(bookingId, userId, labourId)
                            .map(booking -> calculateFinalRating(optionalLabour.get(), reqBody)
                                    .thenCompose(updatedLabour ->
                                        CompletableFuture.supplyAsync(() -> {
                                            try {
                                                labourRepository.save(updatedLabour);
                                                LabourDTO dto = mapEntityToDto(updatedLabour);
                                                return new ResponseDTO(dto, false, "Rated Successfully");
                                            } catch (Exception e) {
                                                return new ResponseDTO(null, true, "Failed to save rating: " + e.getMessage());
                                            }
                                        }, executorService)
                                    ))
                            .orElse(CompletableFuture.completedFuture(
                                new ResponseDTO(null, true, "No booking found for the given details")
                            ));
                })
                .exceptionally(throwable ->
                        new ResponseDTO(null, true, "Failed to process rating: " + throwable.getMessage())
                );
    }

    @Async
    public CompletableFuture<Labour> calculateFinalRating(Labour labour, Map<String, Object> reqBody) {
        return CompletableFuture.supplyAsync(() -> {
                    Integer userId = (Integer) reqBody.get("userId");
                    Integer labourId = (Integer) reqBody.get("labourId");
                    double rating = Double.parseDouble((reqBody.get("labourRating")).toString());
                    String review = (String) reqBody.get("review");

                    rating = Math.round(rating * 10) / 10.0;

                    String ratingCountStr = labour.getRatingCount();
                    String ratingStr = labour.getRating();

                    int storedRatingCount = 0;
                    double storedRating = 0.0;

                    if (ratingCountStr != null && !ratingCountStr.isEmpty()) {
                        storedRatingCount = Integer.parseInt(ratingCountStr);
                    }

                    if (ratingStr != null && !ratingStr.isEmpty()) {
                        storedRating = Double.parseDouble(ratingStr);
                    }

                    storedRating = ((storedRating * storedRatingCount) + rating) / (storedRatingCount + 1);
                    double roundedstoredRating = Math.round(storedRating * 10) / 10.0;

                    return new Object[]{roundedstoredRating, storedRatingCount};
                }, executorService)
                .thenApply(params -> {
                    Integer userId = (Integer) reqBody.get("userId");
                    double rating = Double.parseDouble((reqBody.get("labourRating")).toString());
                    String review = (String) reqBody.get("review");

                    double roundedstoredRating = (double) params[0];
                    int storedRatingCount = (int) params[1];

                    labour.setRating(Double.toString(roundedstoredRating));
                    labour.setRatingCount(Integer.toString(storedRatingCount + 1));

                    Review newReview = new Review();
                    newReview.setUserId(userId);
                    newReview.setRating(rating);
                    newReview.setReview(review);
                    newReview.setLabour(labour);
                    newReview.setReviewTime(LocalDate.now());
                    labour.addReviews(newReview);

                    return labour;
                });
    }



    public CompletableFuture<ResponseDTO> setBookingStatus(Integer labourId, Integer bookingId, Integer bookingStatusCode){


           Optional<Bookings> myBooking = bookingRepository.findById(bookingId);

           if(myBooking.isPresent()){
                Bookings b = myBooking.get();
                b.setBookingStatusCode(bookingStatusCode);
                bookingRepository.save(b);
               return CompletableFuture.completedFuture(new ResponseDTO(b, false, "Status updated Successfully !!"));
           }

        return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Unable to change status l̥!!"));



    }




    @Async
    public  CompletableFuture<ResponseDTO> showRequestedServices(Integer labourId){

             Optional<List<Bookings>> requests = bookingRepository.findByLabourId(labourId);

             if(requests.isPresent()){
                    return CompletableFuture.completedFuture(new ResponseDTO(requests.get(),false,"Requests fetched successfully !!"));
             }

        return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Unable to fetch requests!!"));


    }





    private LabourDTO mapEntityToDto(Labour labour) {
        LabourDTO dto = new LabourDTO();
        dto.setLabourId(labour.getLabourId());
        dto.setLabourName(labour.getLabourName());
        dto.setLabourSkill(labour.getLabourSkill());
        dto.setLabourMobileNo(labour.getLabourMobileNo());
        dto.setRating(labour.getRating());
        dto.setRatingCount(labour.getRatingCount());
        dto.setReviews(labour.getReviews());

        return dto;
    }

    private Labour mapDtoToEntity(LabourDTO dto) {
        Labour labour = new Labour();
        labour.setLabourName(dto.getLabourName());
        labour.setLabourSkill(dto.getLabourSkill());
        labour.setLabourMobileNo(dto.getLabourMobileNo());
        labour.setRating(dto.getRating());
        labour.setRatingCount(dto.getRatingCount());
        labour.setReviews(dto.getReviews());
        return labour;
    }
}

