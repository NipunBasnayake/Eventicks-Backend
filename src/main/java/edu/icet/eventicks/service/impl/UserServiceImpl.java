package edu.icet.eventicks.service.impl;

import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.service.UserService;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class UserServiceImpl implements UserService {

    @Override
    public ResponseEntity<Boolean> addUser(UserDto userDto) {
        return null;
    }

    @Override
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> updateUser(UserDto userDto) {
        return null;
    }

    @Override
    public ResponseEntity<Boolean> deleteUser(Long id) {
        return null;
    }
}
