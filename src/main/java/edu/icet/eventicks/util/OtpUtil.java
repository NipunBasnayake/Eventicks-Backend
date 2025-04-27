package edu.icet.eventicks.util;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class OtpUtil {
    public String generateOtp() {
        Random random = new Random();
        int otpNumber = 100000 + random.nextInt(900000);
        return String.valueOf(otpNumber);
    }
}