package com.example.lab10.controller;

import com.example.lab10.dto.UserRequestDto;
import com.example.lab10.dto.UserResponseDto;
import com.example.lab10.security.UserPrincipal;
import com.example.lab10.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.access.prepost.PreAuthorize;
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

    /**
     * GET /users
     * Returns all users
     * Headers: Accept (optional)
     * Returns: 200 OK with list of users
     */
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestHeader(value = "Accept", required = false, defaultValue = "application/json") String accept) {
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * GET /users/me
     * Returns current authenticated user
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.getUserById(principal.getId()));
    }

    /**
     * GET /users/{id}
     * Returns a user by ID
     * Headers: Accept (optional)
     * Returns: 200 OK with user, 404 if not found
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> getUserById(
            @PathVariable Integer id,
            @RequestHeader(value = "Accept", required = false, defaultValue = "application/json") String accept) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    /**
     * POST /users
     * Creates a new user from JSON body
     * Headers: Content-Type: application/json
     * Returns: 201 Created with user, 400 if validation fails, 415 if wrong Content-Type
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> createUser(
            @Valid @RequestBody UserRequestDto userRequest,
            @RequestHeader(value = "Content-Type", required = false) String contentType) {
        UserResponseDto created = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(created);
    }

    /**
     * POST /users/form
     * Creates a new user from form data
     * Headers: Content-Type: application/x-www-form-urlencoded
     * Returns: 201 Created with user, 400 if validation fails
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/form", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponseDto> createUserFromForm(
            @Valid @ModelAttribute UserRequestDto userRequest) {
        UserResponseDto created = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(created);
    }

    /**
     * PUT /users/{id}
     * Updates an existing user
     * Headers: Content-Type: application/json
     * Returns: 200 OK with updated user, 400 if validation fails, 404 if not found
     */
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Integer id,
            @Valid @RequestBody UserRequestDto userRequest,
            @RequestHeader(value = "Content-Type", required = false) String contentType) {
        UserResponseDto updated = userService.updateUser(id, userRequest);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(updated);
    }

    /**
     * DELETE /users/{id}
     * Deletes a user by ID
     * Returns: 200 OK with success message, 404 if not found
     */
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
    }

    /**
     * GET /users/hello
     * Simple hello endpoint
     * Headers: User-Agent (optional)
     * Returns: 200 OK with greeting message
     */
    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(
            @RequestHeader(value = "User-Agent", required = false) String userAgent) {
        String message = "Hello, user!";
        if (userAgent != null) {
            message += " Your User-Agent: " + userAgent;
        }
        return ResponseEntity.ok(Map.of("message", message));
    }
}
