package com.example.labourApp.Service.ServiceImpl;

import com.example.labourApp.Models.OTPReqDTO;
import com.example.labourApp.Service.OTPRateLimiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
public class OTPRateLimiterServiceImpl implements OTPRateLimiterService {

    @Autowired
    private RedisTemplate<String, OTPReqDTO> redisTemplate;

    @Value("${spring.otp.2FACTOR_API_KEY}")
    String API_KEY;

    private static final int MAX_REQUESTS = 2;
    private static final int TIME_WINDOW_SECONDS = 15;
    private static final int BLOCK_DURATION_SECONDS = 3600;

//    public boolean isBlocked(String phone) {
//        OTPReqDTO info = redisTemplate.opsForValue().get(phone);
//        return info != null && info.getBlockUntil() != null && Instant.now().isBefore(info.getBlockUntil());
//    }

    public boolean registerRequest(String phone) {
        Instant now = Instant.now();
        OTPReqDTO info = redisTemplate.opsForValue().get(phone);

        if (info == null) {
            info = new OTPReqDTO();
        }

        // Clean old entries
        info.getRequestTimes().removeIf(t -> t.isBefore(now.minusSeconds(TIME_WINDOW_SECONDS)));

        // Already blocked
        if (info.getBlockUntil() != null && now.isBefore(info.getBlockUntil())) {
            return false;
        }

        // Check limit
        if (info.getRequestTimes().size() >= MAX_REQUESTS) {
            info.setBlockUntil(now.plusSeconds(BLOCK_DURATION_SECONDS));
            redisTemplate.opsForValue().set(phone, info);
            return false;
        }

        // Add new request
        info.getRequestTimes().add(now);
        redisTemplate.opsForValue().set(phone, info);
        return true;
    }

    public String verifyOtp(String phone,String otp) {
        String url = "https://2factor.in/API/V1/" + API_KEY + "/SMS/VERIFY/" + phone + "/" + otp;
        RestTemplate restTemplate = new RestTemplate();
        try {
            String response = restTemplate.getForObject(url, String.class);
            return "OTP verification result: " + response;
        } catch (Exception e) {
            return "Error verifying OTP: " + e.getMessage();
        }
    }


}
