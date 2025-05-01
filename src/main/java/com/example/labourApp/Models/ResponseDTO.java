package com.example.labourApp.Models;

import lombok.*;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDTO {
    public Object returnValue;
    public Boolean hasError;
    public String message;
}
