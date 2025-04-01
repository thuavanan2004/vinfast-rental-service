package com.vinfast.rental_service.dtos.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class PromotionRequest {

    @NotBlank(message = "Promotion code is required")
    @Size(max = 50, message = "Promotion code must not exceed 50 characters")
    private String code;

    @NotBlank(message = "Promotion name is required")
    @Size(max = 100, message = "Promotion name must not exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Discount type is required")
    @Pattern(regexp = "percentage|fixed_amount", message = "Discount type must be 'percentage' or 'fixed_amount'")
    private String discountType;

    @NotNull(message = "Discount value is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Discount value must be greater than 0")
    private BigDecimal discountValue;

    private Integer minRentalDays;

    @DecimalMin(value = "0.0", message = "Max discount amount must be greater than or equal to 0")
    private BigDecimal maxDiscountAmount;

    @NotNull(message = "Start date is required")
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    private LocalDateTime endDate;

    @NotNull(message = "Usage limit is required")
    @Min(value = 1, message = "Usage limit must be at least 1")
    private Integer usageLimit;

    private Integer timesUsed = 0;

}
