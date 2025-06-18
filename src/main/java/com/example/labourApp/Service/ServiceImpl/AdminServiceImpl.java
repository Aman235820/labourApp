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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public CompletableFuture<ResponseDTO> removeLabour(Integer labourId) {

        Optional<Labour> labourOptional = labourRepository.findById(labourId);

        if (labourOptional.isEmpty()) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Labour not found in Database !!"));
        }

        Labour labour = labourOptional.get();
        
        // Delete all bookings associated with this labour
        Optional<List<Bookings>> bookingsOptional = bookingRepository.findByLabourId(labourId);
        if (bookingsOptional.isPresent() && !bookingsOptional.get().isEmpty()) {
            bookingRepository.deleteAll(bookingsOptional.get());
        }
        
        // Clear the reviews list to ensure proper cascade
        labour.getReviews().clear();
        
        // Clear the labourSubSkills list to ensure proper cascade
        labour.getLabourSubSkills().clear();
        
        // Delete the labour (this will cascade to delete all associated reviews and sub-skills)
        labourRepository.delete(labour);
        
        return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully Deleted Labour and all associated reviews, sub-skills, and bookings"));
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

    @Async
    @Transactional
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

                if (isRowEmpty(row)) break;

                CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {

                    Labour labour = new Labour();
                    labour.setLabourName(row.getCell(0).getStringCellValue());
                    labour.setLabourSkill(row.getCell(1).getStringCellValue());
                    labour.setLabourMobileNo(row.getCell(2).getStringCellValue());

                    labours.add(labour);
                }, executorService);
                futures.add(future);
            }

            // Wait for all futures to complete
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();


            return CompletableFuture.supplyAsync(() -> {
                try {

                    labourRepository.saveAll(labours);
                    labourRepository.flush(); // Important to trigger constraint violations
                    return new ResponseDTO(null, false, "Successfully uploaded all labours from sheet !!");
                } catch (DataIntegrityViolationException ce) {
                    throw new DataIntegrityViolationException("Duplicate mobile number found in the Excel sheet.");
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


        CompletableFuture<Object> skillsStatsFuture = CompletableFuture.supplyAsync(() -> {
            List<Object[]> result = bookingRepository.getSkillsStats();

            HashMap<Object, Object> map = new HashMap<>();

            for (Object[] x : result) {
                map.put(x[0], x[1]);
            }

            return map;

        }, executorService);


        return CompletableFuture.allOf(bookingStatusStatsFuture, labourRatingStatsFuture, skillsStatsFuture)
                .thenApply(__ -> {

                    Object bookingStatusStats = bookingStatusStatsFuture.join();
                    Object labourRatingStats = labourRatingStatsFuture.join();
                    Object skillsStats = skillsStatsFuture.join();

                    HashMap<String, Object> finalStats = new HashMap<>();

                    finalStats.put("bookingStatusStats", bookingStatusStats);
                    finalStats.put("labourRatingStats", labourRatingStats);
                    finalStats.put("availableSkillStats", skillsStats);

                    return new ResponseDTO(finalStats, false, "Successfully Fetched !!");

                });


    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO> clearAllReviews() {
        try {
            // This will properly handle foreign key constraints
            List<Labour> allLabours = labourRepository.findAll();
            for (Labour labour : allLabours) {
                labour.getReviews().clear();
            }
            labourRepository.saveAll(allLabours);
            
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully cleared all reviews"));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Failed to clear reviews: " + e.getMessage()));
        }
    }

    @Async
    @Transactional
    public CompletableFuture<ResponseDTO> truncateLabourTable() {
        try {
            // Use the repository method that handles foreign key constraints
            labourRepository.truncateLabourTable();
            
            return CompletableFuture.completedFuture(new ResponseDTO(null, false, "Successfully truncated Labour table"));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Failed to truncate Labour table: " + e.getMessage()));
        }
    }

    private boolean isRowEmpty(Row row) {
        for (int i = 0; i <= 2; i++) { // check first 3 cells: name, skill, mobile
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
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
