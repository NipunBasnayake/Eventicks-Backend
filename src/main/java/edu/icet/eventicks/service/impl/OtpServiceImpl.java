package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.OtpData;
import edu.icet.eventicks.service.OtpService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final Map<String, OtpData> otpMap = new ConcurrentHashMap<>();
    private final Random random = new SecureRandom();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Value("${eventicks.otp.expiry-minutes:5}")
    private long otpExpiryMinutes;

    @Value("${eventicks.otp.length:6}")
    private int otpLength;

    @PostConstruct
    public void init() {
        // Schedule cleanup task to run every minute
        scheduler.scheduleAtFixedRate(this::cleanupExpiredOtps, 1, 1, TimeUnit.MINUTES);
        log.info("OTP cleanup scheduler initialized with expiry time of {} minutes", otpExpiryMinutes);
    }

    @Override
    public String generateOtp(String email) {
        if (email == null || email.isBlank()) {
            log.error("Cannot generate OTP for null or empty email");
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        // Generate a secure random OTP
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }

        String otpValue = otp.toString();
        long expiryTimeMillis = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(otpExpiryMinutes);

        // Store OTP
        otpMap.put(email, new OtpData(otpValue, expiryTimeMillis));
        log.info("Generated OTP for email: {} (expires at {})", email,
                LocalDateTime.now().plusMinutes(otpExpiryMinutes));

        return otpValue;
    }

    @Override
    public boolean validateOtp(String email, String otp) {
        if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
            log.error("Invalid validateOtp parameters: email={}, otp={}", email, otp);
            return false;
        }

        OtpData otpData = otpMap.get(email);
        if (otpData == null) {
            log.error("No OTP found for email: {}", email);
            return false;
        }

        // Check if OTP is expired
        if (System.currentTimeMillis() > otpData.getExpiryTime()) {
            log.error("OTP expired for email: {}", email);
            otpMap.remove(email); // Remove expired OTP
            return false;
        }

        // Check if OTP matches
        if (!otpData.getOtp().equals(otp)) {
            log.error("Invalid OTP provided for email: {}", email);
            return false;
        }

        log.info("OTP validated successfully for email: {}", email);
        return true;
    }

    @Override
    public void removeOtp(String email) {
        if (email != null && !email.isBlank()) {
            otpMap.remove(email);
            log.info("Removed OTP for email: {}", email);
        }
    }

    @Override
    public void cleanupExpiredOtps() {
        long currentTime = System.currentTimeMillis();
        int count = 0;

        for (Map.Entry<String, OtpData> entry : otpMap.entrySet()) {
            if (currentTime > entry.getValue().getExpiryTime()) {
                otpMap.remove(entry.getKey());
                count++;
            }
        }

        if (count > 0) {
            log.info("Cleaned up {} expired OTPs", count);
        }
    }
}