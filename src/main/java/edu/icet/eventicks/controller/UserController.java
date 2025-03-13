package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.ApiResponseDto;
import edu.icet.eventicks.dto.LoginRequestDto;
import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.dto.UserRegistrationDto;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDto<UserDto>> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        UserDto createdUser = userService.registerUser(registrationDto);
        return new ResponseEntity<>(ApiResponseDto.success("User registered successfully", createdUser), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDto<UserDto>> loginUser(@RequestBody LoginRequestDto loginRequestDto) {
        UserDto authenticatedUser = userService.authenticateUser(loginRequestDto);
        return ResponseEntity.ok(ApiResponseDto.success("Login successful", authenticatedUser));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserDto>> getUserById(@PathVariable Integer userId) {
        UserDto user = userService.getUserById(userId);
        return ResponseEntity.ok(ApiResponseDto.success(user));
    }

    @GetMapping
    public ResponseEntity<ApiResponseDto<List<UserDto>>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponseDto.success(users));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<UserDto>> updateUser(
            @PathVariable Integer userId,
            @RequestBody UserDto userDto) {
        UserDto updatedUser = userService.updateUser(userId, userDto);
        return ResponseEntity.ok(ApiResponseDto.success("User updated successfully", updatedUser));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseDto<Void>> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok(ApiResponseDto.success("User deleted successfully", null));
    }

    @GetMapping("/verify-email/{token}")
    public ResponseEntity<ApiResponseDto<Void>> verifyEmail(@PathVariable String token) {
        userService.verifyEmail(token);
        return ResponseEntity.ok(ApiResponseDto.success("Email verified successfully", null));
    }
}
