package com.example.labourApp.Service;

import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.EnterpriseLabourDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface EnterpriseService {
    CompletableFuture<ResponseDTO> registerEnterprise(EnterpriseDTO enterprise);

    CompletableFuture<ResponseDTO> enterpriseLogin(String mobileNumber);

    CompletableFuture<ResponseDTO> updateEnterpriseField(String id, Map<String, Object> updatedField);

    CompletableFuture<ResponseDTO> findEnterpriseById(String enterpriseId);

    CompletableFuture<ResponseDTO> enterpriseLabourOnboarding(EnterpriseLabourDTO enterpriseLabourDTO);

    CompletableFuture<ResponseDTO> bulkUploadEnterpriseLabour(InputStream inputStream);

    CompletableFuture<ResponseDTO> findLabourByEnterpriseID(String enterpriseId);
}
