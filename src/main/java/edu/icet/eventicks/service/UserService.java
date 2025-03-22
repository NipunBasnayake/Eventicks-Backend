package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserRegistrationDto registrationDto);

    UserDto login(LoginRequestDto loginRequestDto);

    UserDto getUserById(Integer userId);

    List<UserDto> getAllUsers();

    UserDto updateUser(Integer userId, UserDto userDto);

    Boolean deleteUser(Integer userId);

    Boolean verifyEmail(String token);

    Boolean sendOtpEmail(String email);

    Boolean validateOtp(String email, String otp);

    Boolean resetPassword(String email, String newPassword);
}
