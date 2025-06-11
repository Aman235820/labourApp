package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.CustomExceptions.ResourceNotFoundException;
import com.example.labourApp.Entity.Bookings;
import com.example.labourApp.Entity.Labour;
import com.example.labourApp.Entity.User;
import com.example.labourApp.Models.*;
import com.example.labourApp.Repository.BookingRepository;
import com.example.labourApp.Repository.LabourRepository;
import com.example.labourApp.Repository.UserRepository;
import com.example.labourApp.Service.AdminService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Autowired
    LabourRepository labourRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Async
    public CompletableFuture<ResponseDTO> removeLabour(Integer labourId) {

        boolean isAlreadyExists = labourRepository.existsById(labourId);

        if (!isAlreadyExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Labour not found in Database !!"));
        }

        labourRepository.deleteById(labourId);
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Deleted"));
    }

    @Async
    public CompletableFuture<ResponseDTO> removeUser(Integer userId) {

        boolean isAlreadyExists = userRepository.existsById(userId);

        if (!isAlreadyExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "User not found in Database !!"));
        }

        userRepository.deleteById(userId);
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Deleted"));
    }


    @Async
    public CompletableFuture<PaginationResponseDTO> findAllUsers(PaginationRequestDTO paginationRequestDTO) {

        Integer pageNumber = paginationRequestDTO.getPageNumber();
        Integer pageSize = paginationRequestDTO.getPageSize();
        String sortBy = paginationRequestDTO.getSortBy();
        String sortOrder = paginationRequestDTO.getSortOrder();

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> userPage = this.userRepository.findAll(p);

        List<User> userList = userPage.getContent();

        List<UserDTO> dtoList = userList.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(new PaginationResponseDTO(dtoList, userPage.getNumber(), userPage.getSize(),
                userPage.getTotalElements(), userPage.getTotalPages(), userPage.isLast()));
    }

    public CompletableFuture<ResponseDTO> uploadFromExcelFile(MultipartFile myFile) {

        // Create a thread pool with optimal size based on available processors
        int processors = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(processors);

        // Create a thread-safe list to store labours
        List<Labour> labours = Collections.synchronizedList(new ArrayList<>());

        // Create a list to hold all futures
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        try (InputStream is = myFile.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0); // get first sheet

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // skip header row

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                    Labour labour = new Labour();
                    labour.setLabourName(row.getCell(0).getStringCellValue());
                    labour.setLabourSkill(row.getCell(1).getStringCellValue());
                    labour.setLabourMobileNo(String.format("%.0f", row.getCell(2).getNumericCellValue()));

                    labours.add(labour);
                }, executorService);
                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            // Shutdown the executor service
            executorService.shutdown();

            return CompletableFuture.supplyAsync(() -> {
                try {
                    labourRepository.saveAll(labours);
                    return new ResponseDTO(null, false, "Successfully uploaded all labours from sheet !!");
                } catch (Exception e) {
                    return new ResponseDTO(null, true, "Error saving to database: " + e.getMessage());
                }
            });

        } catch (Exception ce) {
            ce.printStackTrace();
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "An Error occurred while uploading labours from sheet : " + ce.getMessage()));
        }
    }

    @Async
    public CompletableFuture<PaginationResponseDTO> getAllBookings(PaginationRequestDTO paginationRequestDTO) {
        Integer pageNumber = paginationRequestDTO.getPageNumber();
        Integer pageSize = paginationRequestDTO.getPageSize();
        String sortBy = paginationRequestDTO.getSortBy();
        String sortOrder = paginationRequestDTO.getSortOrder();

        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable p = PageRequest.of(pageNumber, pageSize, sort);

        Page<Bookings> bookingPage = this.bookingRepository.findAll(p);

        List<Bookings> bookingList = bookingPage.getContent();
        return CompletableFuture.completedFuture(new PaginationResponseDTO(bookingList, bookingPage.getNumber(), bookingPage.getSize(),
                bookingPage.getTotalElements(), bookingPage.getTotalPages(), bookingPage.isLast()));

    }

    @Async
    public CompletableFuture<ResponseEntity<ResponseDTO>> deleteBooking(Integer bookingId) {

        if (bookingRepository.existsById(bookingId)) {
            bookingRepository.deleteById(bookingId);
            return CompletableFuture.completedFuture(new ResponseEntity<>(new ResponseDTO(null, false, "Deleted Successfully !!"), HttpStatus.OK));
        }
        throw new ResourceNotFoundException("Booking", "bookingID", bookingId);
    }


    @Async
    public CompletableFuture<ResponseDTO> getAppStats() {

        CompletableFuture<Map<String, Object>> bookingStatusStatsFuture = CompletableFuture.supplyAsync(() -> {
            Object result = bookingRepository.getBookingStatusStats();
            Object[] row = (Object[]) result;

            Map<String, Object> stats = new HashMap<>();
            stats.put("Rejected", row[0]);
            stats.put("Pending", row[1]);
            stats.put("Accepted", row[2]);
            stats.put("Completed", row[3]);

            return stats;

        }, executorService);

        CompletableFuture<Map<String, Object>> labourRatingStatsFuture = CompletableFuture.supplyAsync(() -> {
            Object result = bookingRepository.getLabourRatingStats();
            Object[] row = (Object[]) result;
            Map<String, Object> stats = new HashMap<>();
            stats.put("rating_5", row[0]);
            stats.put("rating_4", row[1]);
            stats.put("rating_3", row[2]);
            stats.put("rating_2", row[3]);
            stats.put("rating_1", row[4]);

            return stats;

        }, executorService);

        return CompletableFuture.allOf(bookingStatusStatsFuture, labourRatingStatsFuture)
                .thenApply(__ -> {

                    Object bookingStatusStats = bookingStatusStatsFuture.join();
                    Object labourRatingStats = labourRatingStatsFuture.join();

                    HashMap<String, Object> finalStats = new HashMap<>();

                    finalStats.put("bookingStatusStats", bookingStatusStats);
                    finalStats.put("labourRatingStats", labourRatingStats);

                    return new ResponseDTO(finalStats, false, "Successfully Fetched !!");

                });


    }


    private UserDTO mapEntityToDto(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setMobileNumber(user.getMobileNumber());
        return dto;
    }


}
