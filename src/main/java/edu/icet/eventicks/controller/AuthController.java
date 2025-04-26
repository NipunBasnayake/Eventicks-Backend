package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.service.UserService;
import edu.icet.eventicks.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserDto>> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        UserDto createdUser = userService.registerUser(registrationDto);
        return new ResponseEntity<>(ApiResponseDto.success("User registered successfully", createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<UserDto>> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        System.out.println(loginRequestDto.toString());
        UserDto authenticatedUser = userService.login(loginRequestDto);
        if (authenticatedUser != null) {
            String token = jwtUtil.generateToken(authenticatedUser.getEmail());
            authenticatedUser.setToken(token);
            return new ResponseEntity<>(ApiResponseDto.success("Login successful", authenticatedUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(
                    ApiResponseDto.error("Invalid credentials"),
                    HttpStatus.UNAUTHORIZED
            );
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<ApiResponseDto<Boolean>> verifyEmail(@RequestParam String token) {
        boolean verified = userService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponseDto.success("Email verified successfully", verified));
    }

    @PostMapping("/forgot")
    public ResponseEntity<ApiResponseDto<Boolean>> forgotPassword(@RequestParam String email) {
        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body(ApiResponseDto.error("Invalid email"));
        }
        Boolean emailSent = userService.sendOtpEmail(email);
        if (Boolean.TRUE.equals(emailSent)) {
            return ResponseEntity.ok(ApiResponseDto.success("OTP sent to your email", true));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponseDto.error("Failed to send OTP"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<ApiResponseDto<Boolean>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Email and OTP are required"));
        }

        Boolean isValid = userService.validateOtp(email, otp);

        if (Boolean.TRUE.equals(isValid)) {
            return ResponseEntity.ok(ApiResponseDto.success("OTP verified successfully", true));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Invalid or expired OTP"));
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponseDto<Boolean>> resetPassword(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String newPassword = request.get("newPassword");

        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Email and new password are required"));
        }

        Boolean isReset = userService.resetPassword(email, newPassword);

        if (Boolean.TRUE.equals(isReset)) {
            return ResponseEntity.ok(ApiResponseDto.success("Password reset successful", true));
        } else {
            return ResponseEntity.badRequest()
                    .body(ApiResponseDto.error("Failed to reset password"));
        }
    }
}