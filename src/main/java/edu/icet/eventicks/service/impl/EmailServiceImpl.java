package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.password}")
    private String password;

    @Override
    public boolean sendOtpEmail(String to, String name, String otp, long expiryMinutes) {
        try {
            // Validate input parameters
            if (to == null || to.isBlank() || otp == null || otp.isBlank()) {
                log.error("Invalid email parameters: recipient={}, otp={}", to, otp);
                return false;
            }

            log.info("Preparing to send OTP email to: {}", to);

            // Create the email message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set email properties
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Password Reset OTP for EvenTicks");

            // Create HTML content
            String htmlMsg = createOtpEmailTemplate(name, otp, expiryMinutes);
            helper.setText(htmlMsg, true);

            log.info("Sending OTP email to: {}", to);
            mailSender.send(mimeMessage);
            log.info("OTP email successfully sent to: {}", to);
            return true;

        } catch (MessagingException e) {
            log.error("MessagingException while sending OTP email to {}: {}", to, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error sending OTP email to {}: {}", to, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean sendEmailVerification(String to, String name, String verificationLink) {
        try {
            // Validate input parameters
            if (to == null || to.isBlank() || verificationLink == null || verificationLink.isBlank()) {
                log.error("Invalid verification email parameters: recipient={}, link={}", to, verificationLink);
                return false;
            }

            log.info("Preparing to send verification email to: {}", to);

            // Create the email message
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Set email properties
            helper.setFrom(username);
            helper.setTo(to);
            helper.setSubject("Verify Your Email for EvenTicks");

            // Create HTML content
            String htmlMsg = createVerificationEmailTemplate(name, verificationLink);
            helper.setText(htmlMsg, true);

            log.info("Sending verification email to: {}", to);
            mailSender.send(mimeMessage);
            log.info("Verification email successfully sent to: {}", to);
            return true;

        } catch (MessagingException e) {
            log.error("MessagingException while sending verification email to {}: {}", to, e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Unexpected error sending verification email to {}: {}", to, e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean testConnection() {
        log.info("Testing SMTP connection to {}:{} with username: {}", host, port, username);

        try {
            // Cast the JavaMailSender to JavaMailSenderImpl to access its properties
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) mailSender;

            // Get the session
            Session session = mailSenderImpl.getSession();
            session.setDebug(true);

            // Connect to the server
            try (Transport transport = session.getTransport("smtp")) {
                transport.connect(host, port, username, password);
                log.info("SMTP connection test successful!");
                return true;
            }
        } catch (ClassCastException e) {
            log.error("Could not cast JavaMailSender to JavaMailSenderImpl. Using alternative approach.");
            try {
                // Alternative approach with direct connection
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);

                Session session = Session.getInstance(props);
                session.setDebug(true);

                try (Transport transport = session.getTransport("smtp")) {
                    transport.connect(host, port, username, password);
                    log.info("SMTP connection test successful (alternative approach)!");
                    return true;
                }
            } catch (Exception innerEx) {
                log.error("SMTP connection test failed (alternative approach): {}", innerEx.getMessage(), innerEx);
                return false;
            }
        } catch (Exception e) {
            log.error("SMTP connection test failed: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Creates an HTML template for OTP emails
     */
    private String createOtpEmailTemplate(String name, String otp, long expiryMinutes) {
        return "<html>"
                + "<body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<div style='background-color: #f5f5f5; padding: 20px; text-align: center;'>"
                + "<h1 style='color: #4a4a4a;'>EvenTicks</h1>"
                + "</div>"
                + "<div style='padding: 20px; background-color: #ffffff;'>"
                + "<h2 style='color: #4a4a4a;'>Password Reset OTP</h2>"
                + "<p>Dear " + (name != null ? name : "User") + ",</p>"
                + "<p>We received a request to reset your password. To proceed, please use the following One-Time Password (OTP):</p>"
                + "<div style='background-color: #f8f8f8; padding: 15px; text-align: center; margin: 20px 0;'>"
                + "<h2 style='color: #4285f4; letter-spacing: 5px;'>" + otp + "</h2>"
                + "</div>"
                + "<p>This OTP will expire in " + expiryMinutes + " minutes.</p>"
                + "<p>If you did not request a password reset, please ignore this email or contact support if you have concerns.</p>"
                + "<p>Regards,<br/>The EvenTicks Team</p>"
                + "</div>"
                + "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #777;'>"
                + "<p>This is an automated email, please do not reply.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }

    /**
     * Creates an HTML template for verification emails
     */
    private String createVerificationEmailTemplate(String name, String verificationLink) {
        return "<html>"
                + "<body style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;'>"
                + "<div style='background-color: #f5f5f5; padding: 20px; text-align: center;'>"
                + "<h1 style='color: #4a4a4a;'>EvenTicks</h1>"
                + "</div>"
                + "<div style='padding: 20px; background-color: #ffffff;'>"
                + "<h2 style='color: #4a4a4a;'>Verify Your Email</h2>"
                + "<p>Dear " + (name != null ? name : "User") + ",</p>"
                + "<p>Thank you for registering with EvenTicks. To complete your registration, please verify your email address by clicking the button below:</p>"
                + "<div style='text-align: center; margin: 30px 0;'>"
                + "<a href='" + verificationLink + "' style='background-color: #4285f4; color: white; padding: 12px 20px; text-decoration: none; border-radius: 5px; font-weight: bold;'>Verify Email</a>"
                + "</div>"
                + "<p>If the button doesn't work, you can also copy and paste the following link into your browser:</p>"
                + "<p style='word-break: break-all;'><a href='" + verificationLink + "'>" + verificationLink + "</a></p>"
                + "<p>If you did not register with EvenTicks, please ignore this email.</p>"
                + "<p>Regards,<br/>The EvenTicks Team</p>"
                + "</div>"
                + "<div style='background-color: #f5f5f5; padding: 15px; text-align: center; font-size: 12px; color: #777;'>"
                + "<p>This is an automated email, please do not reply.</p>"
                + "</div>"
                + "</body>"
                + "</html>";
    }
}