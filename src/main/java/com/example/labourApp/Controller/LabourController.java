package com.example.labourApp.Controller;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import jakarta.validation.Valid;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/labour")
public class LabourController {


    @Autowired
    private LabourService labourService;

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "Working fine";
    }


    @PostMapping("/registerLabour")
    public Callable<ResponseEntity<ResponseDTO>> registerLabour(
            @Valid @RequestBody LabourDTO details
    ) {
        return () -> {        //returns immediately with a Callable, so the main servlet thread is released
            try {

                CompletableFuture<ResponseDTO> response = labourService.registerLabour(details);

                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to register"), HttpStatus.BAD_REQUEST);
            }
        };


    }

    @GetMapping("/labourLogin")
    public Callable<ResponseEntity<ResponseDTO>> labourLogin(@RequestParam String mobileNumber) {

        return () -> {
            try {

                CompletableFuture<ResponseDTO> response = labourService.labourLogin(mobileNumber);

                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to fetch"), HttpStatus.BAD_REQUEST);
            }
        };

    }

    @GetMapping("/showMyReviews/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> showMyReviews(
            @PathVariable Integer labourId,
            @RequestParam String sortBy,
            @RequestParam String sortOrder
    ) {
        return () -> {
            try {

                CompletableFuture<ResponseDTO> res = labourService.showMyReviews(labourId, sortBy, sortOrder);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Unable to fetch reviews !!"), HttpStatus.BAD_REQUEST);
            }

        };
    }


    @GetMapping("/showRequestedServices/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> showRequestedServices(@PathVariable Integer labourId) {

        return () -> {
            try {

                CompletableFuture<ResponseDTO> response = labourService.showRequestedServices(labourId);

                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to fetch"), HttpStatus.BAD_REQUEST);
            }
        };

    }


    @GetMapping("/setBookingStatus")
    public Callable<ResponseEntity<ResponseDTO>> setBookingStatus(

            @RequestParam Integer labourId,
            @RequestParam Integer bookingId,
            @RequestParam Integer bookingStatusCode

    ) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> response = labourService.setBookingStatus(labourId, bookingId, bookingStatusCode);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to change status"), HttpStatus.BAD_REQUEST);
            }
        };
    }


}
