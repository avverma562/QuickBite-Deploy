package com.example.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.auth.entity.User;
import com.example.auth.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/auth/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/all")
    public java.util.List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return authService.getUserById(id);
    }

    @PutMapping("/ban/{id}")
    public String toggleBan(@PathVariable Integer id) {
        authService.toggleUserBan(id);
        return "User status updated";
    }

    @PutMapping("/approve/{id}")
    public String approveUser(@PathVariable Integer id) {
        authService.approveUser(id);
        return "User approved";
    }

    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {

        String email = authentication.getName();

        return authService.getUserByEmail(email);
    }

    @PutMapping("/profile")
    public User updateProfile(Authentication authentication,
            @RequestBody Map<String, String> request) {

        String email = authentication.getName();
        User user = authService.getUserByEmail(email);

        return authService.updateProfile(
                user.getId(),
                request.get("name"),
                request.get("phone"));
    }

    @PutMapping("/password")
    public String changePassword(Authentication authentication,
            @RequestBody Map<String, String> request) {

        String email = authentication.getName();
        User user = authService.getUserByEmail(email);

        authService.changePassword(
                user.getId(),
                request.get("newPassword"));

        return "Password updated successfully";
    }

    @DeleteMapping("/deactivate")
    public String deactivate(Authentication authentication) {

        String email = authentication.getName();
        User user = authService.getUserByEmail(email);

        authService.deactivateAccount(user.getId());

        return "Account deactivated";
    }
}