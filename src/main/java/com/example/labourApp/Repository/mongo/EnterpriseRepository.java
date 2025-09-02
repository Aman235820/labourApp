package com.example.labourApp.Repository.mongo;

import com.example.labourApp.Entity.mongo.Enterprise;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnterpriseRepository extends MongoRepository<Enterprise, String> {
    boolean existsByOwnerContactInfo(String mobileNo);
}
