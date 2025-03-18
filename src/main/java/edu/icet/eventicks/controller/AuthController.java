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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
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
}
