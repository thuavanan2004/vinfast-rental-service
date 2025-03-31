package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.MaintenanceType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRequest {
    @NotNull(message = "Maintenance type cannot be null")
    private MaintenanceType maintenanceType;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 100, message = "Service center name cannot exceed 100 characters")
    private String serviceCenter;

    @PositiveOrZero(message = "Cost must be positive or zero")
    private BigDecimal cost;

    @NotNull(message = "Start date cannot be null")
    @FutureOrPresent(message = "Start date must be in the present or future")
    private LocalDate startDate;

    @FutureOrPresent(message = "End date must be in the present or future")
    private LocalDate endDate;

    @PositiveOrZero(message = "Mileage must be positive or zero")
    private Integer mileage;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}
