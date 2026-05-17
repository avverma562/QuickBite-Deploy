package com.example.auth.controller;

import com.example.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private Map<String, String> request;

    @BeforeEach
    void setUp() {
        request = new HashMap<>();
    }

    @Test
    void testRegister() {
        request.put("name", "Jane Doe");
        request.put("email", "jane@example.com");
        request.put("password", "pass123");
        request.put("role", "USER");

        when(authService.register("Jane Doe", "jane@example.com", "pass123", "USER"))
                .thenReturn("User Registered Successfully");

        String response = authController.register(request);

        assertEquals("User Registered Successfully", response);
        verify(authService, times(1)).register(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void testLogin() {
        request.put("email", "jane@example.com");
        request.put("password", "pass123");

        when(authService.login("jane@example.com", "pass123")).thenReturn("mock-jwt-token");

        String response = authController.login(request);

        assertEquals("mock-jwt-token", response);
        verify(authService, times(1)).login("jane@example.com", "pass123");
    }

    @Test
    void testForgotPassword() {
        request.put("email", "jane@example.com");

        when(authService.generatePasswordResetToken("jane@example.com")).thenReturn("uuid-token-123");

        Map<String, String> response = authController.forgotPassword(request);

        assertNotNull(response);
        assertEquals("uuid-token-123", response.get("token"));
        assertEquals("Simulated email sent. Use this token to reset your password.", response.get("message"));
    }

    @Test
    void testResetPassword() {
        request.put("token", "uuid-token-123");
        request.put("newPassword", "newPass456");

        doNothing().when(authService).resetPassword("uuid-token-123", "newPass456");

        String response = authController.resetPassword(request);

        assertEquals("Password successfully updated. You can now login.", response);
        verify(authService, times(1)).resetPassword("uuid-token-123", "newPass456");
    }
}
