package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto registerUser(UserRegistrationDto registrationDto) {

        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity userEntity = new UserEntity( );
        userEntity.setUsername(registrationDto.getUsername());
        userEntity.setEmail(registrationDto.getEmail());
        userEntity.setPasswordHash(registrationDto.getPassword());
        userEntity.setRole("USER"); // Default role
        userEntity.setIsEmailVerified(false);
        userEntity.setRegisteredAt(LocalDateTime.now());

        UserEntity savedUser = userRepository.save(userEntity);

        // Generate and send verification email (simplified)
        // emailService.sendVerificationEmail(savedUser.getEmail(), generateVerificationToken(savedUser));

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    @Transactional
    public UserDto authenticateUser(LoginRequestDto loginRequestDto) {
        return null;
    }

    @Override
    public UserDto getUserById(Integer userId) {
       return null;
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }

    @Override
    @Transactional
    public UserDto updateUser(Integer userId, UserDto userDto) {
        return null;
    }

    @Override
    @Transactional
    public void deleteUser(Integer userId) {
    }

    @Override
    @Transactional
    public void verifyEmail(String token) {

    }
}