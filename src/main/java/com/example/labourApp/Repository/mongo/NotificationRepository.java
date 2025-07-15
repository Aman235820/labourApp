package com.example.labourApp.Repository.mongo;

import com.example.labourApp.Entity.mongo.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification, String> {
    
    List<Notification> findByUserId(Integer userId);
    
    List<Notification> findByUserIdAndRead(Integer userId, boolean read);
    
    @Query("{'user_id': ?0, 'type': ?1}")
    List<Notification> findByUserIdAndType(Integer userId, String type);
    
    @Query("{'created_at': {$gte: ?0}}")
    List<Notification> findByCreatedAtAfter(java.time.LocalDateTime dateTime);
    
    long countByUserIdAndRead(Integer userId, boolean read);
} 