package com.example.labourApp.Controller;


import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.AdminService;
import com.example.labourApp.Service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @Autowired
    LabourService labourService;

    @PostMapping("/getAllLabours")
    public Callable<ResponseEntity<PaginationResponseDTO>> getAllLabours(
            @RequestBody PaginationRequestDTO paginationRequestDTO
    ) {
        return () -> {
            try {
                CompletableFuture<PaginationResponseDTO> response = labourService.findAllLabours(paginationRequestDTO);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new PaginationResponseDTO("Failed to get data", 0, 0, 0, 0, true), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @PostMapping("/getAllBookings")
    public Callable<ResponseEntity<PaginationResponseDTO>> getAllBookings(
            @RequestBody PaginationRequestDTO paginationRequestDTO
    ) {
        return () -> {
            try {
                CompletableFuture<PaginationResponseDTO> res = adminService.getAllBookings(paginationRequestDTO);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new PaginationResponseDTO("Failed to get data", 0, 0, 0, 0, true), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @DeleteMapping("/removeLabour/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> removeLabour(@PathVariable Integer labourId) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = adminService.removeLabour(labourId);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @PostMapping("/getAllUsers")
    public Callable<ResponseEntity<PaginationResponseDTO>> getAllUsers(
            @RequestBody PaginationRequestDTO paginationRequestDTO
    ) {
        return () -> {
            try {
                CompletableFuture<PaginationResponseDTO> response = adminService.findAllUsers(paginationRequestDTO);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new PaginationResponseDTO("Failed to get data", 0, 0, 0, 0, true), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @DeleteMapping("/removeUser/{userId}")
    public Callable<ResponseEntity<ResponseDTO>> removeUser(@PathVariable Integer userId) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = adminService.removeUser(userId);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }


    @PostMapping("/uploadLabours")
    public Callable<ResponseEntity<ResponseDTO>> uploadLabours(
            @RequestParam("file") MultipartFile myFile
    ) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = adminService.uploadFromExcelFile(myFile);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @DeleteMapping("/deleteBooking/{bookingId}")
    public Callable<ResponseEntity<ResponseDTO>> deleteBooking(@PathVariable Integer bookingId) {

        return () -> {
            CompletableFuture<ResponseEntity<ResponseDTO>> res = adminService.deleteBooking(bookingId);  // This can throw ResourceNotFoundException
            return res.get();
        };
    }

    @GetMapping("/getAppStats")
    public Callable<ResponseEntity<ResponseDTO>> getAppStats(){
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = adminService.getAppStats();

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }





}
