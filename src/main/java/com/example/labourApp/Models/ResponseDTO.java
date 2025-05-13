package com.example.labourApp.Models;

import lombok.*;

@Data
public class ResponseDTO {
    public Object returnValue;
    public Boolean hasError;
    public String message;

    public ResponseDTO() {
    }

    public ResponseDTO(Object returnValue, Boolean hasError, String message) {
        this.returnValue = returnValue;
        this.hasError = hasError;
        this.message = message;
    }
}
