package com.example.labourApp.Service;


import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.util.concurrent.CompletableFuture;

public interface AdminService {
    CompletableFuture<ResponseDTO> removeLabour(Integer labourId);

    CompletableFuture<ResponseDTO> removeUser(Integer userId);

    CompletableFuture<PaginationResponseDTO> findAllUsers(PaginationRequestDTO paginationRequestDTO);
}
