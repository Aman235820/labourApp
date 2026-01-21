package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.MongoDocumentService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@ConditionalOnProperty(name = "spring.data.mongodb.uri", matchIfMissing = false)
public class MongoDocumentServiceImpl implements MongoDocumentService {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    private MongoDatabase getDatabase() {
        return mongoClient.getDatabase(databaseName);
    }

    private MongoCollection<Document> getCollection(String collectionName) {
        return getDatabase().getCollection(collectionName);
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> createMongoDocument(String collectionName, Map<String, Object> data) {
        try {
            Document document = new Document(data);
            // Add timestamp
            document.put("createdAt", System.currentTimeMillis());

            getCollection(collectionName).insertOne(document);

            return CompletableFuture.completedFuture(
                    new ResponseDTO(document, false, "Document created successfully!")
            );
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to create document: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> findDocumentById(String collectionName, String documentId) {
        try {
            Document document = getCollection(collectionName)
                    .find(Filters.eq("_id", new ObjectId(documentId)))
                    .first();

            if (document != null) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(document, false, "Document found!")
                );
            } else {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(null, true, "Document not found!")
                );
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to find document: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> findDocumentsByField(String collectionName, String fieldName, Object value) {
        try {
            List<Document> documents = new ArrayList<>();
            getCollection(collectionName)
                    .find(Filters.eq(fieldName, value))
                    .into(documents);

            // Convert _id to string
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Document doc : documents) {
                Map<String, Object> map = new HashMap<>(doc);
                if (doc.getObjectId("_id") != null) {
                    map.put("_id", doc.getObjectId("_id").toHexString());
                }
                resultList.add(map);
            }

            return CompletableFuture.completedFuture(
                    new ResponseDTO(resultList, false, "Documents found: " + resultList.size())
            );
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to find documents: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> updateDocument(String collectionName, String documentId, Map<String, Object> updates) {
        try {
            Document updateDoc = new Document();
            for (Map.Entry<String, Object> entry : updates.entrySet()) {
                // Skip _id field to avoid immutable field error
                if (!"_id".equals(entry.getKey())) {
                    updateDoc.put(entry.getKey(), entry.getValue());
                }
            }
            updateDoc.put("updatedAt", System.currentTimeMillis());

            Document result = getCollection(collectionName)
                    .findOneAndUpdate(
                            Filters.eq("_id", new ObjectId(documentId)),
                            new Document("$set", updateDoc)
                    );

            if (result != null) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(result, false, "Document updated successfully!")
                );
            } else {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(null, true, "Document not found for update!")
                );
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to update document: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> deleteDocument(String collectionName, String documentId) {
        try {
            Document result = getCollection(collectionName)
                    .findOneAndDelete(Filters.eq("_id", new ObjectId(documentId)));

            if (result != null) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(result, false, "Document deleted successfully!")
                );
            } else {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(null, true, "Document not found for deletion!")
                );
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to delete document: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> getAllDocuments(String collectionName) {
        try {
            List<Document> documents = new ArrayList<>();
            getCollection(collectionName).find().into(documents);

            return CompletableFuture.completedFuture(
                    new ResponseDTO(documents, false, "All documents retrieved: " + documents.size())
            );
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to retrieve documents: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> findDocumentsByQuery(String collectionName, Map<String, Object> query) {
        try {
            Document queryDoc = new Document(query);
            List<Document> documents = new ArrayList<>();
            getCollection(collectionName).find(queryDoc).into(documents);

            return CompletableFuture.completedFuture(
                    new ResponseDTO(documents, false, "Documents found by query: " + documents.size())
            );
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to find documents by query: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> addFieldToDocument(String collectionName, String documentId, String fieldName, Object value) {
        try {
            Document result = getCollection(collectionName)
                    .findOneAndUpdate(
                            Filters.eq("_id", new ObjectId(documentId)),
                            Updates.set(fieldName, value)
                    );

            if (result != null) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(result, false, "Field added successfully!")
                );
            } else {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(null, true, "Document not found!")
                );
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to add field: " + e.getMessage())
            );
        }
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> removeFieldFromDocument(String collectionName, String documentId, String fieldName) {
        try {
            Document result = getCollection(collectionName)
                    .findOneAndUpdate(
                            Filters.eq("_id", new ObjectId(documentId)),
                            Updates.unset(fieldName)
                    );

            if (result != null) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(result, false, "Field removed successfully!")
                );
            } else {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(null, true, "Document not found!")
                );
            }
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to remove field: " + e.getMessage())
            );
        }
    }
} 