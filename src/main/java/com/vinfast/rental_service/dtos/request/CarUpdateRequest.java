package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.CarStatus;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarUpdateRequest {
    @NotNull(message = "Car model ID cannot be null")
    private Long carModelId;

    @NotBlank(message = "License plate cannot be blank")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "License plate must be alphanumeric with hyphens")
    private String licensePlate;

    @NotBlank(message = "VIN number cannot be blank")
    @Size(min = 10, max = 17, message = "VIN must be 17 characters")
    private String vinNumber;

    @NotBlank(message = "Color cannot be blank")
    private String color;

    @NotNull(message = "Manufacturing date cannot be null")
    @PastOrPresent(message = "Manufacturing date must be in the past or present")
    private LocalDate manufacturingDate;

    @Min(value = 0, message = "Mileage cannot be negative")
    private Integer currentMileage;

    @NotNull(message = "Status cannot be null")
    private CarStatus status;

    private LocalDate lastMaintenanceDate;
    private Integer nextMaintenanceMileage;

    @DecimalMin(value = "0.0", message = "Battery health cannot be negative")
    @DecimalMax(value = "100.0", message = "Battery health cannot exceed 100%")
    private Double batteryHealth;

    private Long pickupLocationId;
}
