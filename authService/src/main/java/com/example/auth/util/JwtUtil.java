package com.example.auth.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	private final String SECRET = "secretKeysecretKeysecretKeysecretKey12345";
	private final long EXPIRATION = 1000*60*60;  // 1 hour
	
	public String generateToken(String email, String role, Integer userId, boolean isApproved) {
		return Jwts
				.builder()
				.setSubject(email)
				.claim("role", role)
				.claim("userId", userId)
				.claim("isApproved", isApproved)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public String generateToken(String email, String role) {
		return Jwts
				.builder()
				.setSubject(email)
				.claim("role", role)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET.getBytes());
	}

	public String extractEmail(String token) {
		return getClaims(token).getSubject();
	}
	
	public String extractRole(String token) {
	    return getClaims(token).get("role", String.class);
	}

	public Integer extractUserId(String token) {
	    return getClaims(token).get("userId", Integer.class);
	}
	
	private Claims getClaims(String token) {
		 return Jwts.parserBuilder()  
	                .setSigningKey(getSigningKey())
	                .build()
	                .parseClaimsJws(token)
	                .getBody();
	}
	
	 public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
	
}
