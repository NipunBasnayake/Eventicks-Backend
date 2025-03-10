package edu.icet.eventicks.service;

import edu.icet.eventicks.dto.UserDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    
    ResponseEntity<Boolean> addUser(UserDto userDto);

    ResponseEntity<List<UserDto>> getAllUsers();

    ResponseEntity<Boolean> updateUser(UserDto userDto);

    ResponseEntity<Boolean> deleteUser(Long id);
}
