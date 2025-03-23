package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.OtpData;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.UserService;
import edu.icet.eventicks.util.EmailUtil;
import edu.icet.eventicks.util.JwtUtil;
import edu.icet.eventicks.util.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final OtpUtil otpUtil;
    private final EmailUtil emailUtil;

    private final Map<String, OtpData> otpStorage = new ConcurrentHashMap<>();
    private static final String USER_NOT_FOUND_MESSAGE = "User not found with ID: ";
    private static final String USER_ID_NULL_MESSAGE = "User ID cannot be null";

    @Override
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

        return modelMapper.map(userRepository.save(userEntity), UserDto.class);
    }

    @Override
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
    public Boolean verifyEmail(String token) {
        if (token == null || token.isEmpty()) {
            return false;
        }

        try {
            JwtUtil jwtUtil = new JwtUtil();
            String email = jwtUtil.extractEmail(token);
            if (email == null) {
                log.error("Failed to extract email from token");
                return false;
            }

            UserEntity user = userRepository.findByEmail(email);
            if (user == null) {
                log.error("No user found with email: {}", email);
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
            log.error("Error verifying email: {}", e.getMessage(), e);
            return false;
        }
    }

    @Override
    public Boolean sendOtpEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            return false;
        }
        String otp = otpUtil.generateOtp();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        otpStorage.put(email, new OtpData(otp, expiryTime));

        return emailUtil.sendOtpEmailToPasswordReset(email, otp);
    }

    @Override
    public Boolean validateOtp(String email, String otp) {
        if (email == null || email.isEmpty() || otp == null || otp.isEmpty()) {
            return false;
        }

        OtpData otpData = otpStorage.get(email);

        if (otpData == null) {
            return false;
        }

        if (otpData.isExpired()) {
            otpStorage.remove(email);
            return false;
        }
        boolean equals = otpData.getOtp().equals(otp);
        log.info("Validating otp: {}", equals);
        return equals;
    }

    @Override
    public Boolean resetPassword(String email, String newPassword) {
        if (email == null || email.isEmpty() || newPassword == null || newPassword.isEmpty()) {
            log.error("Email or new password is null or empty");
            return false;
        }

        // Verify that OTP was validated first
        if (!otpStorage.containsKey(email)) {
            log.error("OTP not validated for email: {}", email);
            return false;
        }

        UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            log.error("No user found with email: {}", email);
            return false;
        }

        try {
            String hashedPassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(hashedPassword);
            userRepository.save(user);
            otpStorage.remove(email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}