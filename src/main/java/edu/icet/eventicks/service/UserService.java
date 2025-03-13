package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserRegistrationDto registrationDto);

    UserDto authenticateUser(LoginRequestDto loginRequestDto);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(Integer userId, UserDto userDto);

    void deleteUser(Integer userId);

    void verifyEmail(String token);
}
