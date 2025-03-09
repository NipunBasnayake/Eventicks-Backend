package edu.icet.eventicks.dto;

import edu.icet.eventicks.util.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserDto {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    private boolean isEmailVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime registeredAt;
}
