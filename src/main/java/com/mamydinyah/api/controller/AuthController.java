package com.mamydinyah.api.controller;

import com.mamydinyah.api.dto.AuthResponse;
import com.mamydinyah.api.dto.LoginRequest;
import com.mamydinyah.api.dto.RegisterRequest;
import com.mamydinyah.api.entity.User;
import com.mamydinyah.api.security.jwt.JWTGenerator;
import com.mamydinyah.api.security.key.ApiKeyGenerator;
import com.mamydinyah.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth Controller", description = "API to manage authentication")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTGenerator jwtGenerator;

    @Operation(
            summary = "Register user",
            description = "Register a new user with name, email, and password",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User registered successfully"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "409", description = "Email already in use")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Validation error: " + bindingResult.getFieldError().getDefaultMessage()));
        }

        Optional<User> existingUser = Optional.ofNullable(userService.findUserByEmail(registerRequest.getEmail()));
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Email already in use"));
        }

        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setApiKey(ApiKeyGenerator.generateApiKey());
        user.setCreatedAt(new Date());

        userService.saveUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(new AuthResponse("User registered successfully"));
    }

    @Operation(
            summary = "Login user",
            description = "Authenticate a user using their email and password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Login successful"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "401", description = "Invalid email or password")
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest loginRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AuthResponse("Validation error: " + bindingResult.getFieldError().getDefaultMessage()));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            String token = jwtGenerator.generateToken(authentication);

            User authenticatedUser = userService.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            AuthResponse response = new AuthResponse("Login successful");
            response.setToken(token);
            response.setApiKey(authenticatedUser.getApiKey());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new AuthResponse("Invalid email or password"));
        }
    }
}