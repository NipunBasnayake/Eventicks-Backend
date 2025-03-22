package edu.icet.eventicks.service;

public interface OtpService {

    String generateOtp(String email);

    boolean validateOtp(String email, String otp);

    void removeOtp(String email);

    void cleanupExpiredOtps();
}