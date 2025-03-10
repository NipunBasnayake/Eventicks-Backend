package edu.icet.eventicks.controller;

import edu.icet.eventicks.dto.UserDto;
import edu.icet.eventicks.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    
    final UserService userService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addUser (@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> updateUser (@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteUser (@PathVariable Long id) {
        return userService.deleteUser(id);
    }
}
