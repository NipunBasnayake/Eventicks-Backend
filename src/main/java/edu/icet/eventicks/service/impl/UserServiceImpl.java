package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.EmailService;
import edu.icet.eventicks.service.OtpService;
import edu.icet.eventicks.service.UserService;
import edu.icet.eventicks.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final OtpService otpService;
    private final JwtUtil jwtUtil;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found with ID: ";
    private static final String USER_ID_NULL_MESSAGE = "User ID cannot be null";

    @Override
    @Transactional
    public UserDto registerUser(UserRegistrationDto registrationDto) {
        if (registrationDto == null || registrationDto.getEmail() == null || registrationDto.getPassword() == null) {
            throw new IllegalArgumentException("Registration data cannot be null");
        }

        if (userRepository.findByEmail(registrationDto.getEmail()) != null) {
            throw new IllegalArgumentException("Email already in use");
        }

        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());

        UserEntity userEntity = new UserEntity();
        userEntity.setName(registrationDto.getName());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPasswordHash(hashedPassword);
        userEntity.setRole("USER");
        userEntity.setIsEmailVerified(false);
        userEntity.setLastLoginAt(null);
        userEntity.setRegisteredAt(LocalDateTime.now());

        UserEntity savedUser = userRepository.save(userEntity);

        // Send verification email (optionally enable this feature)
        // sendVerificationEmail(savedUser);

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto login(LoginRequestDto loginRequestDto) {
        if (loginRequestDto == null || loginRequestDto.getEmail() == null || loginRequestDto.getPassword() == null) {
            throw new IllegalArgumentException("Login credentials cannot be null");
        }
        UserEntity byEmail = userRepository.findByEmail(loginRequestDto.getEmail());

        if (byEmail == null || !passwordEncoder.matches(loginRequestDto.getPassword(), byEmail.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        byEmail.setLastLoginAt(LocalDateTime.now());
        userRepository.save(byEmail);
        return modelMapper.map(byEmail, UserDto.class);
    }

    @Override
    public UserDto getUserById(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException(USER_ID_NULL_MESSAGE);
        }

        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MESSAGE + userId);
        }

        return modelMapper.map(userOptional.get(), UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = new ArrayList<>();
        userRepository.findAll().forEach(userEntity -> users.add(modelMapper.map(userEntity, UserDto.class)));
        return users;
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer userId, UserDto userDto) {
        if (userId == null) {
            throw new IllegalArgumentException(USER_ID_NULL_MESSAGE);
        }

        if (userDto == null) {
            throw new IllegalArgumentException("User data cannot be null");
        }

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MESSAGE + userId);
        }

        if (userDto.getUserId() != null && !userDto.getUserId().equals(userId)) {
            throw new IllegalArgumentException("Path variable ID doesn't match request body ID");
        }

        userDto.setUserId(userId);
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
        UserEntity byEmail = userRepository.findByEmail(userDto.getEmail());
        userEntity.setPasswordHash(byEmail.getPasswordHash());
        userEntity = userRepository.save(userEntity);

        return modelMapper.map(userEntity, UserDto.class);
    }

    @Override
    @Transactional
    public Boolean deleteUser(Integer userId) {
        if (userId == null) {
            throw new IllegalArgumentException(USER_ID_NULL_MESSAGE);
        }

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException(USER_NOT_FOUND_MESSAGE + userId);
        }

        userRepository.deleteById(userId);
        return true;
    }

    @Override
    @Transactional
    public Boolean verifyEmail(String token) {
        if (token == null || token.isEmpty()) {
            log.error("Email verification failed: Token is null or empty");
            return false;
        }

        try {
            String email = jwtUtil.extractEmail(token);
            if (email == null) {
                log.error("Email verification failed: Could not extract email from token");
                return false;
            }

            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("Email verification failed: No user found with email: {}", email);
                return false;
            }

            if (Boolean.TRUE.equals(user.getIsEmailVerified())) {
                log.info("Email already verified for user: {}", email);
                return true;
            }

            user.setIsEmailVerified(true);
            userRepository.save(user);
            log.info("Email successfully verified for user: {}", email);

            return true;
        } catch (Exception e) {
            log.error("Error verifying email with token: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean sendOtpEmail(String email) {
        try {
            if (email == null || email.isBlank()) {
                log.error("Cannot send OTP: Email is null or empty");
                return false;
            }

            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("Cannot send OTP: No user found with email: {}", email);
                return false;
            }

            // Generate OTP using the OTP service
            String otp = otpService.generateOtp(email);
            log.info("Generated OTP for password reset for user: {}", email);

            // Send OTP via email
            boolean sent = emailService.sendOtpEmail(email, user.getName(), otp, 5);
            if (!sent) {
                log.error("Failed to send OTP email to: {}", email);
                otpService.removeOtp(email);
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("Error in sendOtpEmail: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean validateOtp(String email, String otp) {
        try {
            if (email == null || email.isBlank() || otp == null || otp.isBlank()) {
                log.error("Invalid validateOtp parameters: email={}, otp={}", email, otp);
                return false;
            }

            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("OTP validation failed: No user found with email: {}", email);
                return false;
            }

            // Validate OTP using the OTP service
            return otpService.validateOtp(email, otp);
        } catch (Exception e) {
            log.error("Error validating OTP: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean resetPassword(String email, String newPassword) {
        try {
            if (email == null || email.isBlank() || newPassword == null || newPassword.isBlank()) {
                log.error("Invalid resetPassword parameters: email={}, newPassword={}", email,
                        newPassword != null ? "[PROVIDED]" : null);
                return false;
            }

            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("Password reset failed: No user found with email: {}", email);
                return false;
            }

            // Check if OTP exists and is valid
            if (!otpService.validateOtp(email, "CHECK_ONLY")) {
                log.error("Password reset failed: No validated OTP found for email: {}", email);
                return false;
            }

            // Validate new password
            if (newPassword.length() < 8) {
                log.error("Password reset failed: New password must be at least 8 characters long");
                return false;
            }

            // Update password
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(hashedPassword);
            userRepository.save(user);

            // Remove OTP after successful password reset
            otpService.removeOtp(email);

            log.info("Password reset successful for user: {}", email);
            return true;
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * Sends an email verification link to a user
     *
     * @param user The user entity
     * @return true if successful, false otherwise
     */
    public Boolean sendVerificationEmail(UserEntity user) {
        try {
            if (user == null || user.getEmail() == null) {
                log.error("Cannot send verification email: User or email is null");
                return false;
            }

            String token = jwtUtil.generateToken(user.getEmail());
            String verificationLink = "http://localhost:4200/verify-email?token=" + token;

            return emailService.sendEmailVerification(user.getEmail(), user.getName(), verificationLink);
        } catch (Exception e) {
            log.error("Error sending verification email: {}", e.getMessage(), e);
            return false;
        }
    }
}