package com.vinfast.rental_service.service;


import com.vinfast.rental_service.dtos.request.SignInRequest;
import com.vinfast.rental_service.dtos.response.TokenResponse;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.model.Token;
import com.vinfast.rental_service.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.vinfast.rental_service.enums.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final UserService userService;

    private final UserRepository userRepository;

    public TokenResponse accessToken(SignInRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        tokenService.save(Token.builder().username(user.getEmail()).accessToken(accessToken).refreshToken(refreshToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

    public TokenResponse refreshToken(HttpServletRequest request){
        final String refreshToken = request.getHeader("token");
        if(refreshToken == null || refreshToken.isEmpty()){
            throw new InvalidDataException("Token must be not blank");
        }

        final String email = jwtService.extractUserName(refreshToken, REFRESH_TOKEN);
        var user = userService.getUserByEmail(email);
        if(!jwtService.isTokenValid(refreshToken, REFRESH_TOKEN, user)){
            throw new InvalidDataException("Not allow access with this token");
        }
        String accessToken = jwtService.generateAccessToken(user);
        tokenService.save(Token.builder().username(user.getEmail()).accessToken(accessToken).build());

        return TokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .build();
    }

}
