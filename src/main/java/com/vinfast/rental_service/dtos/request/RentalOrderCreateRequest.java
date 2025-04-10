package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.RentalType;
import com.vinfast.rental_service.validate.EnumPattern;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class RentalOrderCreateRequest {
    @NotNull(message = "userId must be not null")
    private Long userId;

    @NotNull(message = "carId must be not null")
    private Long carId;

    @NotNull(message = "pickupLocationId must be not null")
    private Long pickupLocationId;

    @NotNull(message = "startDateTime must be not null")
    private LocalDateTime startDateTime;

    @NotNull(message = "endDateTime must be not null")
    private LocalDateTime endDateTime;

    @EnumPattern(name = "rentalType", regexp = "daily|weekly|monthly|yearly")
    @NotNull(message = "rentalType must be not null")
    private RentalType rentalType;

    @NotNull(message = "basePrice must be not null")
    private BigDecimal basePrice;

    @NotNull(message = "insuranceOptionId must be not null")
    private Long insuranceOptionId;

    @NotBlank(message = "promotionCode must be not blank")
    private String promotionCode;

    private String specialRequest;
}
