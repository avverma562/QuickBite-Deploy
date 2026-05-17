package com.example.auth.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String fullName;
	
	@Column(unique = true)
	private String email;
	
	private String hashedPassword;
	
	private String phone;
	
	private String role; // Customer, Admin, Delivery, Owner
	
	private String provider; 
	
	private boolean isActive = true;
	
	private boolean isApproved = true; // Default true, will be set to false for AGENT/OWNER in signup
	
	private LocalDateTime createdAt = LocalDateTime.now();
	
	private String profilePicUrl;
	
}
