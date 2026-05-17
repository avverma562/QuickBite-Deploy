package com.example.auth.service;

import com.example.auth.entity.User;

public interface AuthService {
	String register(String name, String email, String password, String role);
	String login(String email, String password);
	User getUserByEmail(String email);
	User getUserById(Integer id);
    User updateProfile(Integer id, String name, String phone);
    void changePassword(Integer id, String newPassword);
    void deactivateAccount(Integer id);
    java.util.List<User> getAllUsers();
    User toggleUserBan(Integer id);
    User approveUser(Integer id);
    String generatePasswordResetToken(String email);
    void resetPassword(String token, String newPassword);
}
