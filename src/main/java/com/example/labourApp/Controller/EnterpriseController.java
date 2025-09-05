package com.example.labourApp.Controller;


import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Security.JwtHelper;
import com.example.labourApp.Security.OtpService;
import com.example.labourApp.Service.EnterpriseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    EnterpriseService enterpriseService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtHelper jwtUtil;


    @PostMapping("/registerEnterprise")
    public Callable<ResponseEntity<?>> registerEnterprise(
            @Valid @RequestBody EnterpriseDTO enterprise,
            @RequestParam String otp
    ) {
        return () -> {
            try {

                String cachedOtp = otpService.getOtp(enterprise.getOwnerContactInfo());
                if (otp.equals(cachedOtp)) {                                                    //If you call getOtp from within the same bean (as in verifyOtp), the cache proxy is bypassed, and the annotation is ignored. This is a well-known limitation of Spring's proxy-based AOP.
                    String role = otpService.getUserRole(enterprise.getOwnerContactInfo());
                    String token = jwtUtil.generateToken(enterprise.getOwnerContactInfo(), role);
                    otpService.clear(enterprise.getOwnerContactInfo());

                    // Get current date and time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    enterprise.setRegistrationTime(formattedDateTime);

                    CompletableFuture<ResponseDTO> response = enterpriseService.registerEnterprise(enterprise);
                    Object returnValue = response.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Registration failed : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @GetMapping("/enterpriseLogin")
    public Callable<ResponseEntity<?>> enterpriseLogin(
            @RequestParam String mobileNumber,
            @RequestParam String otp
    ) {
        return () -> {
            try {

                String cachedOtp = otpService.getOtp(mobileNumber);
                if (otp.equals(cachedOtp)) {

                    String role = otpService.getUserRole(mobileNumber);
                    String token = jwtUtil.generateToken(mobileNumber, role);
                    otpService.clear(mobileNumber);

                    CompletableFuture<ResponseDTO> response = enterpriseService.enterpriseLogin(mobileNumber);
                    Object returnValue = response.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Login failed : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @PatchMapping("/updateEnterpriseField/{id}")
    public Callable<ResponseEntity<ResponseDTO>> updateEnterpriseField(@PathVariable String  id , @RequestBody Map<String,Object> updatedField) {
        return () -> {

            try {
                CompletableFuture<ResponseDTO> res = enterpriseService.updateEnterpriseField(id,updatedField);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to update data : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
            }

        };
    }



}
