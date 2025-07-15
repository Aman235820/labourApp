package com.example.labourApp.Service;

import com.example.labourApp.Models.ResponseDTO;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface MongoDocumentService {
    
    CompletableFuture<ResponseDTO> createMongoDocument(String collectionName, Map<String, Object> data);
    
    CompletableFuture<ResponseDTO> findDocumentById(String collectionName, String documentId);
    
    CompletableFuture<ResponseDTO> findDocumentsByField(String collectionName, String fieldName, Object value);
    
    CompletableFuture<ResponseDTO> updateDocument(String collectionName, String documentId, Map<String, Object> updates);
    
    CompletableFuture<ResponseDTO> deleteDocument(String collectionName, String documentId);
    
    CompletableFuture<ResponseDTO> getAllDocuments(String collectionName);
    
    CompletableFuture<ResponseDTO> findDocumentsByQuery(String collectionName, Map<String, Object> query);
    
    CompletableFuture<ResponseDTO> addFieldToDocument(String collectionName, String documentId, String fieldName, Object value);
    
    CompletableFuture<ResponseDTO> removeFieldFromDocument(String collectionName, String documentId, String fieldName);
} 