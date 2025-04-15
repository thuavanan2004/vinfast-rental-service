package com.vinfast.rental_service.service.Impl;


import com.vinfast.rental_service.dtos.request.RegisterRequest;
import com.vinfast.rental_service.dtos.request.ResetPasswordRequest;
import com.vinfast.rental_service.dtos.request.SignInRequest;
import com.vinfast.rental_service.dtos.request.VerifyOtpRequest;
import com.vinfast.rental_service.dtos.response.TokenResponse;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.model.Token;
import com.vinfast.rental_service.model.User;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.service.JwtService;
import com.vinfast.rental_service.service.UserService;
import com.vinfast.rental_service.service.common.EmailService;
import com.vinfast.rental_service.service.common.OtpService;
import com.vinfast.rental_service.service.common.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.vinfast.rental_service.enums.TokenType.ACCESS_TOKEN;
import static com.vinfast.rental_service.enums.TokenType.REFRESH_TOKEN;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final TokenService tokenService;

    private final UserService userService;

    private final OtpService otpService;

    private final UserRepository userRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

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

    public void removeToken(HttpServletRequest request){
        log.info("---------- logout ----------");

        final String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            throw new InvalidDataException("Token must be not blank");
        }

        final String email = jwtService.extractUserName(token, ACCESS_TOKEN);
        tokenService.delete(email);
    }

    public void register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .build();

        userRepository.save(user);

        log.info("Register user successfully!");
    }

    public void forgotPassword(String email){
        userService.getUserByEmail(email);
        String otp = otpService.generateAndSaveOTP(email);
        emailService.sendOtp(email, otp);

        log.info("Send otp forgot password successfully");
    }

    public String verifyOtp(VerifyOtpRequest request) {
        boolean isValid = otpService.verifyOtp(request.getEmail(), request.getOtp());

        if (!isValid) {
            throw new RuntimeException("OTP is invalid or expired");
        }

        User user = userService.getUserByEmail(request.getEmail());
        return jwtService.generateAccessToken(user);
    }

    public void resetPassword(HttpServletRequest request, ResetPasswordRequest resetPasswordRequest){
        final String token = request.getHeader("token");
        if(token == null || token.isEmpty()){
            throw new InvalidDataException("Token must be not blank");
        }

        if(!resetPasswordRequest.getPassword().equals(resetPasswordRequest.getConfirmPassword())){
            throw new InvalidDataException("Password and confirm password not confirm");
        }

        String email = jwtService.extractUserName(token, ACCESS_TOKEN);
        var user = userService.getUserByEmail(email);
        boolean isValid = jwtService.isTokenValid(token, ACCESS_TOKEN, user);
        if(!isValid){
            throw new InvalidDataException("Token not valid");
        }

        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getPassword()));
        userRepository.save(user);

        log.info("Reset password successfully");
    }
}
