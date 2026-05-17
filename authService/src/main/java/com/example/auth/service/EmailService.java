package com.example.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String token) {
        try {
            String resetLink = frontendUrl + "/reset-password?token=" + token;

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject("QuickBite - Password Reset Request");

            String htmlBody = """
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 20px; background: #0f172a; color: #e2e8f0; border-radius: 12px;">
                        <div style="text-align: center; margin-bottom: 30px;">
                            <h1 style="font-size: 28px; margin: 0;">
                                <span style="background: linear-gradient(135deg, #f97316, #ef4444); -webkit-background-clip: text; -webkit-text-fill-color: transparent;">Quick</span>
                                <span style="color: #e2e8f0;">Bite</span>
                            </h1>
                        </div>
                        <h2 style="color: #f1f5f9; margin-bottom: 16px;">Reset Your Password</h2>
                        <p style="color: #94a3b8; line-height: 1.6; margin-bottom: 24px;">
                            We received a request to reset your QuickBite account password. Click the button below to set a new password.
                            This link will expire in <strong style="color: #f97316;">15 minutes</strong>.
                        </p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="display: inline-block; padding: 14px 32px; background: linear-gradient(135deg, #f97316, #ef4444); color: white; text-decoration: none; border-radius: 8px; font-weight: bold; font-size: 16px;">
                                Reset Password
                            </a>
                        </div>
                        <p style="color: #64748b; font-size: 13px; margin-top: 24px;">
                            If you didn't request this, you can safely ignore this email. Your password will remain unchanged.
                        </p>
                        <hr style="border: none; border-top: 1px solid #1e293b; margin: 24px 0;" />
                        <p style="color: #475569; font-size: 12px; text-align: center;">
                            &copy; 2025 QuickBite. All rights reserved.
                        </p>
                    </div>
                    """.formatted(resetLink);

            helper.setText(htmlBody, true);
            mailSender.send(message);

            System.out.println("Password reset email sent to: " + toEmail);

        } 
        catch (Exception e) {
            System.err.println("Failed to send password reset email: " + e.getMessage());
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
}
