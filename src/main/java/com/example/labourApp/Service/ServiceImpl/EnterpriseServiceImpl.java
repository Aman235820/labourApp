package com.example.labourApp.Service.ServiceImpl;


import com.example.labourApp.Entity.mongo.Enterprise;
import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.mongo.EnterpriseRepository;
import com.example.labourApp.Service.EnterpriseService;
import com.example.labourApp.Service.MongoDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    EnterpriseRepository enterpriseRepository;

    @Autowired
    MongoDocumentService mongoDocumentService;


    @Async
    public CompletableFuture<ResponseDTO> registerEnterprise(EnterpriseDTO details) {

        String mobileNo = details.getOwnerContactInfo();

        boolean isExists = enterpriseRepository.existsByOwnerContactInfo(mobileNo);

        if (isExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Mobile Number already registered, try different !!"));
        }

        // Check if GST number already exists (if provided)
        if (details.getGstNumber() != null && !details.getGstNumber().trim().isEmpty()) {
            boolean gstExists = enterpriseRepository.existsByGstNumber(details.getGstNumber());
            if (gstExists) {
                return CompletableFuture.completedFuture(new ResponseDTO(null, true, "GST Number already registered, try different !!"));
            }
        }

        Enterprise enterprise = mapDtoToEntity(details);
        // Set default values for registration
        enterprise.setVerificationStatus("PENDING");
        enterprise.setRating("0.0");
        enterprise.setRatingCount("0");

        Enterprise savedEnterprise = enterpriseRepository.save(enterprise);
        EnterpriseDTO responseDTO = mapEntityToDto(savedEnterprise);

        return CompletableFuture.completedFuture(new ResponseDTO(responseDTO, false, "Successfully Registered !!"));

    }

    @Async
    public CompletableFuture<ResponseDTO> enterpriseLogin(String mobileNumber) {
        Optional<Enterprise> e = enterpriseRepository.findByOwnerContactInfo(mobileNumber);
        if (e.isPresent()) {
            Enterprise enterprise = e.get();
            EnterpriseDTO enterpriseDTO = mapEntityToDto(enterprise);
            return CompletableFuture.completedFuture(new ResponseDTO(enterpriseDTO, false, "Successfully Fetched enterprise !!"));
        }

        return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Didn't find any enterprise with this mobile number !!"));
    }


    @Async
    public CompletableFuture<ResponseDTO> updateEnterpriseField(String id, Map<String, Object> updatedField) {
        try {
            // Call the generic mongo service to perform the update
            CompletableFuture<ResponseDTO> updateResult = mongoDocumentService.updateDocument("enterprise", id, updatedField);
            ResponseDTO result = updateResult.get();
            
            if (!result.getHasError()) {
                // Create response with only updated fields
                Map<String, Object> responseData = new HashMap<>(updatedField);
                responseData.put("id", id);
                responseData.put("updatedAt", System.currentTimeMillis());
                
                return CompletableFuture.completedFuture(
                    new ResponseDTO(responseData, false, "Enterprise data updated successfully!")
                );
            } else {
                return CompletableFuture.completedFuture(result);
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                new ResponseDTO(null, true, "Failed to update enterprise: " + e.getMessage())
            );
        }
    }


    private Enterprise mapDtoToEntity(EnterpriseDTO dto) {
        Enterprise enterprise = new Enterprise();
        enterprise.setOwnername(dto.getOwnername());
        enterprise.setOwnerContactInfo(dto.getOwnerContactInfo());
        enterprise.setCompanyName(dto.getCompanyName());
        enterprise.setGstNumber(dto.getGstNumber());
        enterprise.setOtherContactNumbers(dto.getOtherContactNumbers());
        enterprise.setRegistrationCertificateLink(dto.getRegistrationCertificateLink());
        enterprise.setWorkforceSize(dto.getWorkforceSize());
        enterprise.setServicesOffered(dto.getServicesOffered());
        enterprise.setOtherDocumentLinks(dto.getOtherDocumentLinks());
        enterprise.setRating(dto.getRating());
        enterprise.setRatingCount(dto.getRatingCount());
        enterprise.setLocation(dto.getLocation());
        enterprise.setVerificationStatus(dto.getVerificationStatus());
        enterprise.setVerifiedAt(dto.getVerifiedAt());
        enterprise.setAdminComments(dto.getAdminComments());
        enterprise.setRegistrationTime(dto.getRegistrationTime());
        return enterprise;
    }

    private EnterpriseDTO mapEntityToDto(Enterprise enterprise) {
        EnterpriseDTO dto = new EnterpriseDTO();
        dto.setId(enterprise.getId());
        dto.setOwnername(enterprise.getOwnername());
        dto.setOwnerContactInfo(enterprise.getOwnerContactInfo());
        dto.setCompanyName(enterprise.getCompanyName());
        dto.setGstNumber(enterprise.getGstNumber());
        dto.setOtherContactNumbers(enterprise.getOtherContactNumbers());
        dto.setRegistrationCertificateLink(enterprise.getRegistrationCertificateLink());
        dto.setWorkforceSize(enterprise.getWorkforceSize());
        dto.setServicesOffered(enterprise.getServicesOffered());
        dto.setOtherDocumentLinks(enterprise.getOtherDocumentLinks());
        dto.setRating(enterprise.getRating());
        dto.setRatingCount(enterprise.getRatingCount());
        dto.setLocation(enterprise.getLocation());
        dto.setVerificationStatus(enterprise.getVerificationStatus());
        dto.setVerifiedAt(enterprise.getVerifiedAt());
        dto.setAdminComments(enterprise.getAdminComments());
        dto.setRegistrationTime(enterprise.getRegistrationTime());

        return dto;
    }

}
