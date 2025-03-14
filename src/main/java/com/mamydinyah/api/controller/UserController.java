package com.mamydinyah.api.controller;

import com.mamydinyah.api.dto.ErrorResponse;
import com.mamydinyah.api.entity.User;
import com.mamydinyah.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "API to manage users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Operation(
            summary = "Get all users",
            responses = {
                @ApiResponse(responseCode = "200", description = "Success"),
                @ApiResponse(responseCode = "404", description = "No users found"),
                @ApiResponse(responseCode = "401", description = "JWT token or API KEY is missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/")
    public ResponseEntity<?> allUsers() {
        List<User> users = userService.allUsers();
        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("No users found"));
        }
        return ResponseEntity.ok(users);
    }

    @Operation(
            summary = "Get user by ID",
            responses = {
                @ApiResponse(responseCode = "200", description = "Success"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "401", description = "JWT token or API KEY is missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> userById(@PathVariable Long id) {
        Optional<User> user = Optional.ofNullable(userService.userById(id));
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found with id: " + id));
        }
        return ResponseEntity.ok(user.get());
    }

    @Operation(
            summary = "Save user",
            responses = {
                @ApiResponse(responseCode = "201", description = "Success"),
                @ApiResponse(responseCode = "400", description = "Bad Request"),
                @ApiResponse(responseCode = "401", description = "JWT token or API KEY is missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PostMapping("/")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        try {
            User savedUser = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("Error saving user: " + e.getMessage()));
        }
    }

    @Operation(
            summary = "Update user",
            responses = {
                @ApiResponse(responseCode = "200", description = "Success"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "401", description = "JWT token or API KEY is missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.userById(id);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found with id: " + id));
        }
        User userToUpdate = existingUser;

        if (user.getName() != null) userToUpdate.setName(user.getName());
        if (user.getEmail() != null) userToUpdate.setEmail(user.getEmail());
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userToUpdate.setApiKey(userToUpdate.getApiKey());
        userToUpdate.setCreatedAt(userToUpdate.getCreatedAt());
        userToUpdate.setUpdatedAt(new Date());
        User updatedUser = userService.updateUser(userToUpdate);
        return ResponseEntity.ok(updatedUser);
    }

    @Operation(
            summary = "Delete user",
            responses = {
                @ApiResponse(responseCode = "200", description = "Success"),
                @ApiResponse(responseCode = "404", description = "User not found"),
                @ApiResponse(responseCode = "401", description = "JWT token or API KEY is missing or invalid"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = Optional.ofNullable(userService.userById(id));
        if (existingUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("User not found with id: " + id));
        }
        userService.deleteUser(id);
        return ResponseEntity.ok(new ErrorResponse("User deleted successfully"));
    }
}