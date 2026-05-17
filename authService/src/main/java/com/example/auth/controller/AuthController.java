package com.example.auth.controller;

import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.example.auth.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        return authService.register(
                request.get("name"),
                request.get("email"),
                request.get("password"),
                request.get("role")
        );
    }

    @PostMapping("/login")
    public String login(@RequestBody Map<String, String> request) {
        return authService.login(
                request.get("email"),
                request.get("password")
        );
    }

    @PostMapping("/forgot-password")
    public java.util.Map<String, String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        // generatePasswordResetToken now sends a real SMTP email internally
        authService.generatePasswordResetToken(email);
        return java.util.Map.of("message", "Password reset email sent! Please check your inbox.");
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        authService.resetPassword(token, newPassword);
        return "Password successfully updated. You can now login.";
    }
}