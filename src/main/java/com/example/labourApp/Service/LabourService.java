package com.example.labourApp.Service;

import com.example.labourApp.Models.LabourDTO;
import com.example.labourApp.Models.ResponseDTO;

import java.util.concurrent.CompletableFuture;

public interface LabourService {

       CompletableFuture<ResponseDTO> registerLabour(LabourDTO details);

       CompletableFuture<ResponseDTO> findLabourByCategory(String category);

       CompletableFuture<ResponseDTO> findAllLabours();
}
