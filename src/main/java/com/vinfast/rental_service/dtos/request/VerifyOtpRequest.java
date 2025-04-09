package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VerifyOtpRequest {
    @NotBlank(message = "Email must not blank")
    private String email;

    @NotBlank(message = "Otp must not blank")
    private String otp;

}
