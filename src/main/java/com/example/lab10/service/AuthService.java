package com.example.lab10.service;

import com.example.lab10.Repository.UserRepository;
import com.example.lab10.dto.UserResponseDto;
import com.example.lab10.dto.auth.LoginRequest;
import com.example.lab10.dto.auth.RegisterRequest;
import com.example.lab10.entity.User;
import com.example.lab10.security.JwtService;
import com.example.lab10.security.UserPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponseDto register(RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("ROLE_USER");

        User saved = userRepository.save(user);

        UserResponseDto dto = new UserResponseDto();
        dto.id = saved.getId();
        dto.username = saved.getUsername();
        dto.email = saved.getEmail();
        return dto;
    }

    public String login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail());
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // safe message
            throw new IllegalArgumentException("Invalid credentials");
        }

        return jwtService.generateToken(new UserPrincipal(user));
    }
}

