package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Models.ResponseDTO;
import com.example.labourApp.Service.MongoDocumentService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.apache.commons.codec.language.Soundex;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Pattern;

@Service
@ConditionalOnProperty(name = "spring.data.mongodb.uri", matchIfMissing = false)
public class MongoDocumentServiceImpl implements MongoDocumentService {

    private static final Soundex SOUNDEX = new Soundex();
    private static final DoubleMetaphone DOUBLE_METAPHONE = new DoubleMetaphone();
    private static final Pattern NON_WORD = Pattern.compile("[^\\p{L}\\p{N}]+");

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

    /**
     * Collects every category name and sub-service string plus word tokens for phonetic match.
     */
    private static void flattenServicesOffered(Object node, Set<String> fullStrings, Set<String> wordTokens) {
        if (node == null) {
            return;
        }
        if (node instanceof String s) {
            String t = s.trim();
            if (!t.isEmpty()) {
                fullStrings.add(t);
                addTokens(t.toLowerCase(Locale.ROOT), wordTokens);
            }
            return;
        }
        if (node instanceof List<?> list) {
            for (Object o : list) {
                flattenServicesOffered(o, fullStrings, wordTokens);
            }
            return;
        }
        if (node instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                if (e.getKey() != null) {
                    String k = e.getKey().toString().trim();
                    if (!k.isEmpty()) {
                        fullStrings.add(k);
                        addTokens(k.toLowerCase(Locale.ROOT), wordTokens);
                    }
                }
                flattenServicesOffered(e.getValue(), fullStrings, wordTokens);
            }
            return;
        }
        if (node instanceof Document doc) {
            for (String key : doc.keySet()) {
                String k = key.trim();
                if (!k.isEmpty()) {
                    fullStrings.add(k);
                    addTokens(k.toLowerCase(Locale.ROOT), wordTokens);
                }
                flattenServicesOffered(doc.get(key), fullStrings, wordTokens);
            }
        }
    }

    private static void addTokens(String lowerText, Set<String> wordTokens) {
        for (String w : NON_WORD.split(lowerText)) {
            if (!w.isEmpty()) {
                wordTokens.add(w);
            }
        }
    }

    private static String soundexOrEmpty(String word) {
        if (word == null || word.length() < 2) {
            return "";
        }
        return SOUNDEX.soundex(word);
    }

    /** Soundex (aligned with SQL-style phonetic match) plus Double Metaphone for close variants (e.g. z/s). */
    private static boolean phoneticWordMatch(String queryWord, String corpusWord) {
        if (queryWord.length() < 2 || corpusWord.length() < 2) {
            return false;
        }
        String q = soundexOrEmpty(queryWord);
        if (!q.isEmpty() && q.equals(soundexOrEmpty(corpusWord))) {
            return true;
        }
        return DOUBLE_METAPHONE.isDoubleMetaphoneEqual(queryWord, corpusWord, false);
    }

    /**
     * Case-insensitive substring on full labels, plus Soundex match on any word (e.g. {@code gizar} ~ {@code Geyser}).
     */
    private static boolean servicesOfferedMatchesSearch(Object servicesOffered, String rawSearch) {
        if (servicesOffered == null || rawSearch == null) {
            return false;
        }
        String needle = rawSearch.trim().toLowerCase(Locale.ROOT);
        if (needle.isEmpty()) {
            return false;
        }

        Set<String> fullStrings = new LinkedHashSet<>();
        Set<String> corpusWords = new LinkedHashSet<>();
        flattenServicesOffered(servicesOffered, fullStrings, corpusWords);

        for (String fs : fullStrings) {
            if (fs.toLowerCase(Locale.ROOT).contains(needle)) {
                return true;
            }
        }

        List<String> queryWords = new ArrayList<>();
        for (String w : NON_WORD.split(needle)) {
            if (!w.isEmpty()) {
                queryWords.add(w);
            }
        }
        if (queryWords.isEmpty()) {
            return false;
        }

        for (String qw : queryWords) {
            if (qw.length() < 2) {
                continue;
            }
            for (String cw : corpusWords) {
                if (phoneticWordMatch(qw, cw)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Map<String, Object>> documentsToMapsWithStringIds(List<Document> documents) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Document doc : documents) {
            Map<String, Object> map = new HashMap<>(doc);
            if (doc.getObjectId("_id") != null) {
                map.put("_id", doc.getObjectId("_id").toHexString());
            }
            resultList.add(map);
        }
        return resultList;
    }

    @Async
    @Override
    public CompletableFuture<ResponseDTO> findDocumentsByServicesOffered(String collectionName, String fieldName, String searchTerm) {
        try {
            if (fieldName == null || fieldName.trim().isEmpty()) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(new ArrayList<>(), false, "No field name provided"));
            }
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return CompletableFuture.completedFuture(
                        new ResponseDTO(new ArrayList<>(), false, "No search term provided"));
            }
            String mapField = fieldName.trim();
            String term = searchTerm.trim();
            List<Document> candidates = new ArrayList<>();
            getCollection(collectionName)
                    .find(Filters.and(Filters.exists(mapField), Filters.ne(mapField, null)))
                    .into(candidates);

            List<Document> documents = new ArrayList<>();
            for (Document doc : candidates) {
                if (servicesOfferedMatchesSearch(doc.get(mapField), term)) {
                    documents.add(doc);
                }
            }

            List<Map<String, Object>> resultList = documentsToMapsWithStringIds(documents);
            String message = resultList.isEmpty()
                    ? "No documents matched this service or sub-service (substring / phonetic)"
                    : "Documents matched (substring or phonetic): " + resultList.size();

            return CompletableFuture.completedFuture(new ResponseDTO(resultList, false, message));
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                    new ResponseDTO(null, true, "Failed to find documents by services offered: " + e.getMessage()));
        }
    }
} 