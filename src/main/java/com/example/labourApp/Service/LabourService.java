package com.example.labourApp.Service;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface LabourService {

    CompletableFuture<ResponseDTO> registerLabour(LabourDTO details);

    CompletableFuture<PaginationResponseDTO> findLabourByCategory(PaginationRequestDTO paginationRequestDTO, String category);

    CompletableFuture<PaginationResponseDTO> findAllLabours(PaginationRequestDTO paginationRequestDTO);

    CompletableFuture<ResponseDTO> rateLabour(Map<String, Object> reqBody);

    CompletableFuture<ResponseDTO> findLabour(Integer labourId);

    CompletableFuture<ResponseDTO> setBookingStatus(Integer labourId, Integer bookingId, Integer bookingStatusCode);

    CompletableFuture<ResponseDTO> showRequestedServices(Integer labourId);

    CompletableFuture<ResponseDTO> labourLogin(String mobileNumber);

    CompletableFuture<ResponseDTO> showMyReviews(Integer labourId, String sortBy, String sortOrder);

    CompletableFuture<ResponseDTO> updateLabourDetails(LabourDTO labourDTO);

    CompletableFuture<ResponseDTO> showMyRatings(Integer labourId);

    CompletableFuture<ResponseDTO> updateAdditionalLabourData(Map<String, Object> details);

    CompletableFuture<ResponseDTO> getAdditionalLabourDetails(Integer labourId);

    void saveProfileImagetoDB(String imageUrl, Integer labourId);

    void removeProfileImage(Integer labourId);
}
