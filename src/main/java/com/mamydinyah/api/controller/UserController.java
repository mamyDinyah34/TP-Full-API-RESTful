package com.mamydinyah.api.controller;

import com.mamydinyah.api.entity.User;
import com.mamydinyah.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userService.allUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> userById(@PathVariable Long id) {
        Optional<User> user = Optional.ofNullable(userService.userById(id));
        return user.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.saveUser(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        Optional<User> existingUser = Optional.ofNullable(userService.userById(id));
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        user.setId(id);
        return ResponseEntity.ok(userService.updateUser(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Optional<User> existingUser = Optional.ofNullable(userService.userById(id));
        if (existingUser.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted");
    }
}
