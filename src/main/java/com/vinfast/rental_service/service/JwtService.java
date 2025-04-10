package com.vinfast.rental_service.service;

import com.vinfast.rental_service.enums.TokenType;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {
    String extractUserName(String token, TokenType type);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userDetails);

    String generateResetToken(UserDetails userDetails);

    boolean isTokenValid(String token, TokenType type, UserDetails userDetails);

    Claims extractAllClaims(String token, TokenType type);
}
