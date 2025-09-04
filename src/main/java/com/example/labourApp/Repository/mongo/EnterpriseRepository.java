package com.example.labourApp.Repository.mongo;

import com.example.labourApp.Entity.mongo.Enterprise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EnterpriseRepository extends MongoRepository<Enterprise, String> {
    
    boolean existsByOwnerContactInfo(String mobileNo);
    
    Optional<Enterprise> findByOwnerContactInfo(String mobileNo);
    
    Optional<Enterprise> findByCompanyName(String companyName);
    
    boolean existsByGstNumber(String gstNumber);
}
