package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for testing email functionality
 * Should be disabled in production
 */
@RestController
@RequestMapping("/test-email")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:4200")
public class TestEmailController {

    private final EmailService emailService;

    @GetMapping("/test-connection")
    public ResponseEntity<ApiResponseDto<Boolean>> testConnection() {
        log.info("Testing email connection");
        boolean result = emailService.testConnection();

        if (result) {
            return ResponseEntity.ok(ApiResponseDto.success("Email connection test successful", true));
        } else {
            return ResponseEntity.ok(ApiResponseDto.error("Email connection test failed"));
        }
    }

    @PostMapping("/send-test")
    public ResponseEntity<ApiResponseDto<Boolean>> sendTestEmail(@RequestBody Map<String, String> request) {
        String to = request.get("email");
        String name = request.get("name");

        if (to == null || to.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Email address is required"));
        }

        log.info("Sending test email to: {}", to);
        boolean result = emailService.sendOtpEmail(to, name != null ? name : "Test User", "123456", 5);

        if (result) {
            return ResponseEntity.ok(ApiResponseDto.success("Test email sent successfully", true));
        } else {
            return ResponseEntity.ok(ApiResponseDto.error("Failed to send test email"));
        }
    }
}