package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.RentalType;
import com.vinfast.rental_service.validate.EnumPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDateTime;

@Getter
@Builder
public class CarAvailabilityRequest {
    @NotBlank(message = "city must be not blank")
    private String city;

    @NotNull(message = "pickupTime must be not null")
    private LocalDateTime pickupTime;

    @NotNull(message = "returnTime must be not null")
    private LocalDateTime returnTime;

    @EnumPattern(name = "rentalType", regexp = "daily|weekly|monthly")
    @NotNull(message = "rentalType must be not null")
    private RentalType rentalType;
}
