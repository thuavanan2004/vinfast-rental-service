package com.vinfast.rental_service.controllers.client;


import com.vinfast.rental_service.dtos.request.RegisterRequest;
import com.vinfast.rental_service.dtos.request.SignInRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
