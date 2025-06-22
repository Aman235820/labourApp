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

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }

    public Boolean getHasError() {
        return hasError;
    }

    public void setHasError(Boolean hasError) {
        this.hasError = hasError;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
