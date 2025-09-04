package com.example.labourApp.Service;

import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.util.concurrent.CompletableFuture;

public interface EnterpriseService {
    CompletableFuture<ResponseDTO> registerEnterprise(EnterpriseDTO enterprise);

    CompletableFuture<ResponseDTO> enterpriseLogin(String mobileNumber);
}
