package com.example.labourApp.Controller;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.LabourService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/app/Labour")
public class LabourController {


    @Autowired
    private LabourService labourService;

    @GetMapping("/healthCheck")
    public String healthCheck() {
        return "Working fine";
    }


    @PostMapping("/registerLabour")
    public Callable<ResponseEntity<ResponseDTO>>registerLabour(
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


}
