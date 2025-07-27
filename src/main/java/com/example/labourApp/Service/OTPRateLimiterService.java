package com.example.labourApp.Service;

public interface OTPRateLimiterService {


    boolean registerRequest(String phone);
}
