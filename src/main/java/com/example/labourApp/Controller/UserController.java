package com.example.labourApp.Controller;

import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("app/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/userLogin")
    public Callable<ResponseEntity<ResponseDTO>> userLogin(@RequestBody UserDTO request) {
        return () -> {
            try {
                CompletableFuture<ResponseDTO> res = userService.loginUser(request);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, "Failed to get user"), HttpStatus.BAD_REQUEST);
            }
        };

    }

    @PostMapping("/registerUser")
    public Callable<ResponseEntity<ResponseDTO>> createUser(@RequestBody UserDTO request) {
        return () -> {
            try {

                CompletableFuture<ResponseDTO> res = userService.createUser(request);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }
        };
    }

    @PostMapping("/rateLabour")
    public Callable<ResponseEntity<ResponseDTO>> rateLabour(
            @RequestBody Map<String, Object> reqBody
    ) {
        return () -> {

            try {

                double ratingNumber = (double) reqBody.get("labourRating");

                if(ratingNumber<0 || ratingNumber>5){
                       throw new Exception("Rate between 0 to 5 only !!");
                }

                CompletableFuture<ResponseDTO> res = userService.rateLabour(reqBody);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }

        };

    }


}
