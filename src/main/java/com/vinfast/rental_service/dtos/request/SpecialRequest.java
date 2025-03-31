package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialRequest {
    @NotBlank(message = "Special request must not blank")
    private String specialRequest;
}
