package com.example.labourApp.Security;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OtpService {

    @CachePut(value = "otpCache", key = "#mobile")
    public String generateOtp(String mobile, String role) {
        String otp = "0000";
        userRoleCache.put(mobile, role);
        return otp;
    }

    @Cacheable(value = "otpCache", key = "#mobile")
    public String getOtp(String mobile) {
        return null; // Spring fetches from cache
    }

    public boolean verifyOtp(String mobile, String otp) {
        String cachedOtp = getOtp(mobile);
        return otp.equals(cachedOtp);
    }

    @CacheEvict(value = "otpCache", key = "#mobile")
    public void clear(String mobile) {
        userRoleCache.remove(mobile);
    }

    private final Map<String, String> userRoleCache = new ConcurrentHashMap<>();

    public String getUserRole(String mobile) {
        return userRoleCache.get(mobile);
    }
}
