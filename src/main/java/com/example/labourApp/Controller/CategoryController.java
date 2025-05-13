package com.example.labourApp.Controller;


import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/app")
public class CategoryController {

    @Autowired
    private LabourService labourService;

    @GetMapping("/findByCategory")
    public Callable<ResponseEntity<ResponseDTO>> findByCategory(@RequestParam String category) {
        return()-> {
            try {
                CompletableFuture<ResponseDTO> response = labourService.findLabourByCategory(category);
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get data"), HttpStatus.BAD_REQUEST);
            }
        };

    }

    @GetMapping("/getAllLabours")
    public Callable<ResponseEntity<ResponseDTO>> getAllLabours() {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> response = labourService.findAllLabours();
                return new ResponseEntity<>(response.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get data"), HttpStatus.BAD_REQUEST);
            }
        };
    }


}
