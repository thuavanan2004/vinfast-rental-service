package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequest {
    @NotBlank(message = "password must not blank")
    private String password;

    @NotBlank(message = "confirm password must not blank")
    private String confirmPassword;
}
