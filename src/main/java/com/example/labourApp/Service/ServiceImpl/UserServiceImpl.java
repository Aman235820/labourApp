package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.Review;
import com.example.labourApp.Entity.User;
import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    ObjectMapper mapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    LabourRepository labourRepository;

    @Async
    public CompletableFuture<ResponseDTO> createUser(UserDTO request) {

        User user = mapper.convertValue(request, User.class);

        userRepository.save(user);

        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Registered Successfully"));

    }


    @Async
    public CompletableFuture<ResponseDTO> loginUser(UserDTO request) {

        String email = request.getEmail();
        String password = request.getPassword();

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "User not found !!"));
        }

        User user = userOptional.get();
        if (!user.getPassword().equals(password)) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Wrong password !!"));
        }

        Map<String, String> result = new HashMap<>();
        result.put("name", user.getFullName());
        result.put("email", user.getEmail());

        return CompletableFuture.completedFuture(new ResponseDTO(result, false, "Successfully logged In !!"));
    }

    @Async
    public CompletableFuture<ResponseDTO> rateLabour(Map<String, Object> reqBody) {
        Integer userId = (Integer) reqBody.get("userId");
        Integer labourId = (Integer) reqBody.get("labourId");
        double rating = (Double) reqBody.get("labourRating");
        String review = (String) reqBody.get("review");

        return CompletableFuture.supplyAsync(() ->
                        labourRepository.findById(labourId), executorService)
                .thenCompose(optionalLabour -> {
                    if (optionalLabour.isEmpty()) {
                        return CompletableFuture.completedFuture(
                                new ResponseDTO(null, true, "Labour not found")
                        );
                    }

                    return calculateFinalRating(optionalLabour.get(), reqBody)
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
                            );
                })
                .exceptionally(throwable ->
                        new ResponseDTO(null, true, "Failed to process rating: " + throwable.getMessage())
                );
    }

    public CompletableFuture<Labour> calculateFinalRating(Labour labour, Map<String,Object> reqBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Integer userId = (Integer) reqBody.get("userId");
                Integer labourId = (Integer) reqBody.get("labourId");
                double rating = (Double) reqBody.get("labourRating");
                String review = (String) reqBody.get("review");

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

                labour.setRating(Double.toString(roundedstoredRating));
                labour.setRatingCount(Integer.toString(storedRatingCount + 1));
                if (review!=null) {
                    Review newReview = new Review();
                    newReview.setUserId(userId);
                    newReview.setRating(rating);
                    newReview.setReview(review);
                    newReview.setLabour(labour);
                    labour.addReviews(newReview);
                }

                return labour;
            } catch (Exception e) {
                throw new RuntimeException("Error calculating rating: " + e.getMessage());
            }
        }, executorService);
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

}
