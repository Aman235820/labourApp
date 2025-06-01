package com.example.labourApp.Service;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface LabourService {

       CompletableFuture<ResponseDTO> registerLabour(LabourDTO details);

       CompletableFuture<PaginationResponseDTO> findLabourByCategory(PaginationRequestDTO paginationRequestDTO , String category);

       CompletableFuture<PaginationResponseDTO> findAllLabours(PaginationRequestDTO paginationRequestDTO);

       CompletableFuture<ResponseDTO> rateLabour(Map<String, Object> reqBody);

       CompletableFuture<ResponseDTO> findLabour(Integer labourId);


    CompletableFuture<ResponseDTO> setBookingStatus(Integer labourId, Integer bookingId, Integer bookingStatusCode);
}
