package com.example.lab10.controller;

import com.example.lab10.dto.UserResponseDto;
import com.example.lab10.entity.User;
import com.example.lab10.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody User user) {
        User created = userService.createUser(user);

        UserResponseDto dto = new UserResponseDto();
        dto.id = created.getId();
        dto.username = created.getUsername();
        dto.email = created.getEmail();

        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        boolean authenticated = userService.authenticateUser(
                credentials.get("email"),
                credentials.get("password")
        );

        return authenticated
                ? ResponseEntity.ok("Login successful")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello, user!";
    }
}
