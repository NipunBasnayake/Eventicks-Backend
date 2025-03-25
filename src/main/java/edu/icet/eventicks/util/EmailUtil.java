package edu.icet.eventicks.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Slf4j
@Component
public class EmailUtil {

    private final String host = "smtp.gmail.com";
    private final String user = "eventicks@gmail.com";
    private final String password = "xxxxx";

    public boolean sendOtpEmailToPasswordReset(String recipientEmail, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
            message.setSubject("Reset Your Password - Eventicks");

            String emailContent = "<html><body>"
                    + "<h2>Eventicks</h2>"
                    + "<p>Hello,</p>"
                    + "<p>You requested to reset your password at Eventicks. Please use the following OTP to proceed:</p>"
                    + "<h2 style='color: #007BFF;'>" + otp + "</h2>"
                    + "<p>If you did not request a password reset, please ignore this email.</p>"
                    + "<p>Thank you for choosing Eventicks!</p>"
                    + "<p>Best Regards,<br/>The Eventicks Team</p>"
                    + "</body></html>";

            message.setContent(emailContent, "text/html");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            return false;
        }
    }
}