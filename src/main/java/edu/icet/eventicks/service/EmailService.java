package edu.icet.eventicks.service;

public interface EmailService {

    boolean sendOtpEmail(String to, String name, String otp, long expiryMinutes);

    boolean sendEmailVerification(String to, String name, String verificationLink);

    boolean testConnection();
}