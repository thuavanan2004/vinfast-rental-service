package com.vinfast.rental_service.controllers.client;


import com.vinfast.rental_service.dtos.request.AdminLoginRequest;
import com.vinfast.rental_service.dtos.request.SignInRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.AdminService;
import com.vinfast.rental_service.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Operation
    @PostMapping("/access-token")
    public ResponseData<?> login(@Valid @RequestBody SignInRequest request){
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
}
