package com.vinfast.rental_service.controllers.client;


import com.vinfast.rental_service.dtos.request.RegisterRequest;
import com.vinfast.rental_service.dtos.request.ResetPasswordRequest;
import com.vinfast.rental_service.dtos.request.SignInRequest;
import com.vinfast.rental_service.dtos.request.VerifyOtpRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.Impl.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Client authentication")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/client/auth")
@RequiredArgsConstructor
public class ClientAuthenticationController {
    private final AuthenticationService authenticationService;

    @Operation(summary = "Api login get access token ")
    @PostMapping("/access-token")
    public ResponseData<?> access(@Valid @RequestBody SignInRequest request){
        log.info("Get access token");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get access token successfully",
                    authenticationService.accessToken(request));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get access token failed");
        }
    }

    @Operation(summary = "Refresh token for get new access token")
    @PostMapping("/refresh-token")
    public ResponseData<?> refresh(HttpServletRequest request){
        log.info("Get refresh token");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Refresh token successfully",
                    authenticationService.refreshToken(request));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Refresh token failed");
        }
    }

    @Operation(summary = "Api for logout")
    @PostMapping("/remove-token")
    public ResponseData<?> remove(HttpServletRequest request){
        log.info("Remove token");
        try{
            authenticationService.removeToken(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Remove token successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Remove token failed");
        }
    }

    @Operation(summary = "Api for register new user")
    @PostMapping("/register")
    public ResponseData<?> register(@Valid @RequestBody RegisterRequest request){
        log.info("Register new user");
        try{
            authenticationService.register(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Register new user successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Register failed");
        }
    }

    @Operation(summary = "Api forgot password")
    @PostMapping("/forgot-password")
    public ResponseData<?> forgotPassword(@Valid @RequestBody String email){
        log.info("Forgot password");
        try{
            authenticationService.forgotPassword(email);

            return new ResponseData<>(HttpStatus.OK.value(), "OTP has been sent to your email");
        } catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Forgot password failed");
        }
    }

    @Operation(summary = "Verify otp")
    @PostMapping("/verify-otp")
    public ResponseData<?> verifyOtp(@Valid @RequestBody VerifyOtpRequest request) {
        log.info("Verify otp");
        try {
            String accessToken = authenticationService.verifyOtp(request);
            return new ResponseData<>(HttpStatus.OK.value(), "OTP verified successfully", Map.of("accessToken", accessToken));
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Verify OTP failed");
        }
    }

    @Operation(summary = "Reset password")
    @PostMapping("/reset-password")
    public ResponseData<?> resetPassword(HttpServletRequest request, @Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        log.info("Reset password");
        try {
            authenticationService.resetPassword(request, resetPasswordRequest);
            return new ResponseData<>(HttpStatus.OK.value(), "Reset password successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Reset password failed");
        }
    }

    @Operation(summary = "Social login with google | facebook")
    @PostMapping("/social-login")
    public ResponseData<?> socialAuth(@RequestParam("login-type") String loginType, HttpServletRequest request) {
        log.info("Social login");
        try {
            return new ResponseData<>(HttpStatus.OK.value(), "Social login successfully");
        } catch (Exception e) {
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Social login failed");
        }
    }
}
