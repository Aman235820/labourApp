package com.example.labourApp.Service;


import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

public interface AdminService {
    CompletableFuture<ResponseDTO> removeLabour(Integer labourId);

    CompletableFuture<ResponseDTO> removeUser(Integer userId);

    CompletableFuture<PaginationResponseDTO> findAllUsers(PaginationRequestDTO paginationRequestDTO);

    CompletableFuture<ResponseDTO> uploadFromExcelFile(MultipartFile myFile);

    CompletableFuture<PaginationResponseDTO> getAllBookings(PaginationRequestDTO paginationRequestDTO);

    CompletableFuture<ResponseEntity<ResponseDTO>> deleteBooking(Integer bookingId);

    CompletableFuture<ResponseDTO> getAppStats();

    CompletableFuture<ResponseDTO> clearAllReviews();

    CompletableFuture<ResponseDTO> truncateLabourTable();
}
