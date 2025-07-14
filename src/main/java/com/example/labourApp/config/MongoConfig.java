package com.example.labourApp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;


@Configuration
@EnableMongoRepositories(basePackages = "com.example.Repository.mongo")  // MongoDB repositories
public class MongoConfig {
    // Spring Boot will auto-configure MongoTemplate and connection
    // You can define a custom MongoClient here if needed
}
