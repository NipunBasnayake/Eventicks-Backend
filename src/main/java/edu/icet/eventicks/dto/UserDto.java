package edu.icet.eventicks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;
    private String name;
    private String email;
    private String role;
    private Boolean isEmailVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime registeredAt;
    private String token;
}