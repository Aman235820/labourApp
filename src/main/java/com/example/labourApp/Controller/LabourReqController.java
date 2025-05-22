package com.example.labourApp.Controller;


import com.example.labourApp.Models.PaginationRequestDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/app")
public class LabourReqController {

    @Autowired
    private LabourService labourService;

    @PostMapping("/findByCategory")
    public Callable<ResponseEntity<PaginationResponseDTO>> findByCategory(@RequestParam String category,
                                                                          @RequestBody PaginationRequestDTO paginationRequestDTO) {
        return () -> {
            try {
                CompletableFuture<PaginationResponseDTO> response = labourService.findLabourByCategory(paginationRequestDTO, category);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new PaginationResponseDTO(ce.getMessage(), 0, 0, 0, 0, true), HttpStatus.BAD_REQUEST);
            }
        };

    }

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

    @GetMapping("getLabourById/{labourId}")
    public Callable<ResponseEntity<ResponseDTO>> getLabourById(@PathVariable Integer labourId) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> response = labourService.findLabour(labourId);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get data"), HttpStatus.BAD_REQUEST);
            }
        };
    }


}
