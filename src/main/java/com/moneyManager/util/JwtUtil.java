package com.moneyManager.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private static final String secretKey ="832re7-yrjer-hehy-dbdfee-dfduife6628";
	
	private static final long EXPIRATION_TIME = 60 * 60 * 60 * 1000;
	
	private static final Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
	
	public String generateToken(String email) {
		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String extractUsername(String token) {
		return extractClaims(token).getSubject();
	}
	
    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return (username.equals(extractedUsername) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }
	
}
