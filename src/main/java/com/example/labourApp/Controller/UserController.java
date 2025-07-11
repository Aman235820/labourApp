package com.example.labourApp.Controller;

import com.example.labourApp.Models.BookingDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Models.UserDTO;
import com.example.labourApp.Service.LabourService;
import com.example.labourApp.Service.UserService;
import org.aspectj.weaver.ast.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private LabourService labourService;

    @PostMapping("/bookLabour")
    public Callable<ResponseEntity<ResponseDTO>> bookLabour(
            @RequestBody BookingDTO bookingDetails
    ) {
        return () -> {

            try {

                // Get current date and time
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String formattedDateTime = now.format(formatter);

                bookingDetails.setBookingTime(formattedDateTime);

                CompletableFuture<ResponseDTO> res = userService.bookLabourService(bookingDetails);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                ce.printStackTrace();
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

                double ratingNumber = Double.parseDouble((reqBody.get("labourRating")).toString());

                if (ratingNumber < 0.0 || ratingNumber > 5.0) {
                    throw new Exception("Rate between 0 to 5 only !!");
                }

                CompletableFuture<ResponseDTO> res = labourService.rateLabour(reqBody);
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }

        };

    }

    @GetMapping("/viewMyBookings/{userId}")
    public Callable<ResponseEntity<ResponseDTO>> viewMyBookings(@PathVariable Integer userId) {
        return () -> {
            try {

                CompletableFuture<ResponseDTO> res = userService.viewMyBookings(userId);

                return new ResponseEntity<>(res.get(), HttpStatus.OK);

            } catch (Exception ce) {
                return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
            }

        };

    }


}
