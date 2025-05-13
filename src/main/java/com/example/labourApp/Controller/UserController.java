package com.example.labourApp.Controller;

import com.example.labourApp.Models.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app/user")
public class UserController {

  /*  @PostMapping("/userLogin")
    public ResponseEntity<ResponseDTO> userLogin(@RequestBody UserDTO request) {

        try {

        } catch (Exception ce) {

        }

    }


    @PostMapping("/registerUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody UserDTO request) {
        try {

            ResponseDTO res =

            return new ResponseEntity<>(new ResponseDTO(res, false, "Success"), HttpStatus.CREATED);
        } catch (Exception ce) {
            return new ResponseEntity<>(new ResponseDTO(null, true, ce.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }*/

}
