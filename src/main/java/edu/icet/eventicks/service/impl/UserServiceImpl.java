package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.entity.UserEntity;
import edu.icet.eventicks.repository.UserRepository;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDto registerUser(UserRegistrationDto registrationDto) {
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
        UserEntity byEmail = userRepository.findByEmail(loginRequestDto.getEmail());
        if (byEmail != null && passwordEncoder.matches(loginRequestDto.getPassword(), byEmail.getPasswordHash())) {
            return modelMapper.map(byEmail, UserDto.class);
        }
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
    public UserDto updateUser(Integer userId, UserDto userDto) {
        return null;
    }

    @Override
    public Boolean deleteUser(Integer userId) {
        return false;
    }

    @Override
    public Boolean verifyEmail(String token) {
        return false;
    }
}