package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public UserDto registerUser(UserRegistrationDto registrationDto) {
        return null;
    }

    @Override
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
    public UserDto updateUser(Integer userId, UserDto userDto) {
        return null;
    }

    @Override
    public void deleteUser(Integer userId) {

    }

    @Override
    public void verifyEmail(String token) {

    }
}
