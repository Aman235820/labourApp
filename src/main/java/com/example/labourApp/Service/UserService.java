package com.example.labourApp.Service;

import com.example.labourApp.Models.BookingDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface UserService {

    CompletableFuture<ResponseDTO> createUser(UserDTO request);

    CompletableFuture<ResponseDTO> loginUser(UserDTO request);

    CompletableFuture<ResponseDTO> bookLabourService(BookingDTO bookingDetails);
}