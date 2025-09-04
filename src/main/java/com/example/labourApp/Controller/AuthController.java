package com.example.labourApp.Controller;


import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Security.JwtHelper;
import com.example.labourApp.Security.OtpRequestDTO;
import com.example.labourApp.Security.OtpService;
import com.example.labourApp.Service.LabourService;
import com.example.labourApp.Service.UserService;
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
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private LabourService labourService;

    @Autowired
    UserService userService;

    @Autowired
    private OtpService otpService;

    @Autowired
    private JwtHelper jwtUtil;


    @PostMapping("/requestOTP")
    public Callable<ResponseEntity<?>> requestOTP(@Valid @RequestBody OtpRequestDTO req) {
        return () -> {
            String otp = otpService.generateOtp(req.getMobile(), req.getRole());
            return new ResponseEntity(new ResponseDTO(null, false, "OTP sent to " + req.getMobile()), HttpStatus.OK);
        };
    }

    //labour

    @PostMapping("/registerLabour")
    public Callable<ResponseEntity<?>> registerLabour(
            @Valid @RequestBody LabourDTO details,
            @RequestParam String otp
    ) {
        return () -> {
            try {
                String cachedOtp = otpService.getOtp(details.getLabourMobileNo());
                if (otp.equals(cachedOtp)) {                                                    //If you call getOtp from within the same bean (as in verifyOtp), the cache proxy is bypassed, and the annotation is ignored. This is a well-known limitation of Spring's proxy-based AOP.
                    String role = otpService.getUserRole(details.getLabourMobileNo());
                    String token = jwtUtil.generateToken(details.getLabourMobileNo(), role);
                    otpService.clear(details.getLabourMobileNo());

                    // Get current date and time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    details.setRegistrationTime(formattedDateTime);

                    CompletableFuture<ResponseDTO> response = labourService.registerLabour(details);
                    Object returnValue = response.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to register"), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @GetMapping("/labourLogin")
    public Callable<ResponseEntity<?>> labourLogin(@RequestParam String mobileNumber,
                                                   @RequestParam String otp) {

        return () -> {
            try {

                String cachedOtp = otpService.getOtp(mobileNumber);
                if (otp.equals(cachedOtp)) {                                                    //If you call getOtp from within the same bean (as in verifyOtp), the cache proxy is bypassed, and the annotation is ignored. This is a well-known limitation of Spring's proxy-based AOP.
                    String role = otpService.getUserRole(mobileNumber);
                    String token = jwtUtil.generateToken(mobileNumber, role);
                    otpService.clear(mobileNumber);
                    CompletableFuture<ResponseDTO> response = labourService.labourLogin(mobileNumber);
                    Object returnValue = response.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to fetch"), HttpStatus.BAD_REQUEST);
            }
        };

    }


    //user

    @PostMapping("/userLogin")
    public Callable<ResponseEntity<?>> userLogin(@RequestBody UserDTO request , @RequestParam String otp) {
        return () -> {
            try {

                String cachedOtp = otpService.getOtp(request.getMobileNumber());
                if (otp.equals(cachedOtp)) {                                                    //If you call getOtp from within the same bean (as in verifyOtp), the cache proxy is bypassed, and the annotation is ignored. This is a well-known limitation of Spring's proxy-based AOP.
                    String role = otpService.getUserRole(request.getMobileNumber());
                    String token = jwtUtil.generateToken(request.getMobileNumber(), role);
                    otpService.clear(request.getMobileNumber());
                    CompletableFuture<ResponseDTO> res = userService.loginUser(request);
                    Object returnValue = res.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get user"), HttpStatus.BAD_REQUEST);
            }
        };

    }

    @PostMapping("/registerUser")
    public Callable<ResponseEntity<?>> createUser(@RequestBody UserDTO request , @RequestParam String otp) {
        return () -> {
            try {

                String cachedOtp = otpService.getOtp(request.getMobileNumber());
                if (otp.equals(cachedOtp)) {                                                    //If you call getOtp from within the same bean (as in verifyOtp), the cache proxy is bypassed, and the annotation is ignored. This is a well-known limitation of Spring's proxy-based AOP.
                    String role = otpService.getUserRole(request.getMobileNumber());
                    String token = jwtUtil.generateToken(request.getMobileNumber(), role);
                    otpService.clear(request.getMobileNumber());

                    // Get current date and time
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                    String formattedDateTime = now.format(formatter);

                    request.setRegistrationTime(formattedDateTime);

                    CompletableFuture<ResponseDTO> res = userService.createUser(request);
                    if(res.get().getHasError()){
                        throw new Exception("Already a registered mobile number , try different !!");
                    }
                    Object returnValue = res.get().getReturnValue();
                    Map<String, Object> map = Map.of("returnValue", returnValue,
                            "token", token
                    );
                    return new ResponseEntity<>(map, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new ResponseDTO(null, false, "Wrong OTP !!"), HttpStatus.OK);
                }

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @GetMapping("/generateTestToken")
    public ResponseEntity<String> generateTestToken(@RequestParam String mobile, @RequestParam String role) {
        String token = jwtUtil.generateToken(mobile, role);
        return ResponseEntity.ok(token);
    }

}
