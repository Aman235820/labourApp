package com.example.labourApp.Service.ServiceImpl;


import com.example.labourApp.Entity.mongo.Enterprise;
import com.example.labourApp.Models.EnterpriseDTO;
import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Repository.mongo.EnterpriseRepository;
import com.example.labourApp.Service.EnterpriseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EnterpriseServiceImpl implements EnterpriseService {

    @Autowired
    EnterpriseRepository enterpriseRepository;


    @Async
    public CompletableFuture<ResponseDTO> registerEnterprise(EnterpriseDTO details){

        String mobileNo = details.getOwnerContactInfo();

        boolean isExists = enterpriseRepository.existsByOwnerContactInfo(mobileNo);

        if (isExists) {
            return CompletableFuture.completedFuture(new ResponseDTO(null, true, "Mobile Number already registered, try different !!"));
        }

        Enterprise enterprise = mapDtoToEntity(details);
        enterpriseRepository.save(enterprise);
        return CompletableFuture.completedFuture(new ResponseDTO(enterprise, false, "Successfully Registered !!"));

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
        
        return dto;
    }

}
