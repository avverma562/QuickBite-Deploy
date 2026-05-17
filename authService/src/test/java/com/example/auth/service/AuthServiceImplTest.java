package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthServiceImpl authService;

    private User testUser;
    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        
        testUser = new User();
        testUser.setId(1);
        testUser.setFullName("John Doe");
        testUser.setEmail("john@example.com");
        testUser.setHashedPassword(encoder.encode("password123"));
        testUser.setRole("CUSTOMER");
        testUser.setActive(true);
        testUser.setApproved(true);
    }

    @Test
    void testRegister_NewUser() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(new User());

        String result = authService.register("New User", "new@example.com", "pass123", "CUSTOMER");
        
        assertEquals("User Registered Successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegister_ExistingUser_ThrowsException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.register("John Doe", "john@example.com", "pass123", "CUSTOMER");
        });

        assertEquals("User already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLogin_ValidCredentials() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        when(jwtUtil.generateToken(anyString(), anyString(), anyInt(), anyBoolean())).thenReturn("mocked-jwt-token");

        String token = authService.login("john@example.com", "password123");

        assertEquals("mocked-jwt-token", token);
    }

    @Test
    void testLogin_InvalidPassword_ThrowsException() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("john@example.com", "wrongpassword");
        });

        assertEquals("Invalid Password", exception.getMessage());
    }

    @Test
    void testLogin_DeactivatedAccount_ThrowsException() {
        testUser.setActive(false);
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login("john@example.com", "password123");
        });

        assertEquals("Account is deactivated", exception.getMessage());
    }

    @Test
    void testGeneratePasswordResetToken_ValidEmail() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));

        String token = authService.generatePasswordResetToken("john@example.com");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void testResetPassword_ValidToken() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(testUser));
        String token = authService.generatePasswordResetToken("john@example.com");

        authService.resetPassword(token, "newPassword456");

        verify(userRepository, times(1)).save(testUser);
        assertTrue(encoder.matches("newPassword456", testUser.getHashedPassword()));
    }

    @Test
    void testResetPassword_InvalidToken_ThrowsException() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.resetPassword("invalid-token-123", "newPassword456");
        });

        assertEquals("Invalid or expired reset token", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
