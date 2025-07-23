package com.example.labourApp.Externals;

import com.cloudinary.Api;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class CDNService {

    private final Cloudinary cloudinary;

    public CDNService(
            @Value("${cloudinary.cloud_name}") String cloudName,
            @Value("${cloudinary.api_key}") String apiKey,
            @Value("${cloudinary.api_secret}") String apiSecret) {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    }

    public String uploadImage(MultipartFile file, Integer labourId) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "public_id", "labour/" + labourId + "/" + UUID.randomUUID().toString(),  // or custom file name
                        "transformation", new com.cloudinary.Transformation().quality("50") // Compress up to 50%
                )
        );

        return (String) uploadResult.get("secure_url");
    }

    public void deleteAllImagesByLabourId(Integer labourId) throws Exception {
        Api api = cloudinary.api();
        api.deleteResourcesByPrefix("labour/" + labourId, ObjectUtils.emptyMap());
    }

}
