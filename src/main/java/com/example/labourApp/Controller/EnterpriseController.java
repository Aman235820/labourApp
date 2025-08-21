package com.example.labourApp.Controller;


import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.PaginationResponseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/enterprise")
public class EnterpriseController {

    @Autowired
    EnterpriseService enterpriseService;


      @PostMapping("/registerEnterprise")
      public Callable<ResponseEntity<ResponseDTO>> registerEnterprise(
              @RequestBody EnterpriseDTO enterprise
      ){
          return()->{
              try {

                  CompletableFuture<ResponseDTO> response = enterpriseService.registerEnterprise(enterprise);

                  return new ResponseEntity<>(response.get(), HttpStatus.OK);

              } catch (Exception ce) {
                  return new ResponseEntity<>(new ResponseDTO(null,true,"Registration failed : " + ce.getMessage()), HttpStatus.BAD_REQUEST);
              }
          };


      }



}
