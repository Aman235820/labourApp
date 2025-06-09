package com.example.labourApp.CustomExceptions;

import com.example.labourApp.Models.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ResponseDTO(null, true, ex.getMessage()), HttpStatus.NOT_FOUND);
    }

}
