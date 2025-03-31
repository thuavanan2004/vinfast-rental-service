package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.CarStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CarCreateRequest {
    @NotBlank(message = "License plate cannot be empty")
    private String licensePlate;

    @NotBlank(message = "VIN number cannot be empty")
    private String vinNumber;

    @NotBlank(message = "Color cannot be empty")
    private String color;

    @NotNull(message = "Manufacturing date cannot be null")
    private Date manufacturingDate;

    @Min(value = 0, message = "Current mileage must be greater than or equal to 0")
    private Integer currentMileage = 0;

    private CarStatus status;

    private Date lastMaintenanceDate;
    private Integer nextMaintenanceMileage;

    @DecimalMin(value = "0.00", message = "Battery health must be at least 0%")
    @DecimalMax(value = "100.00", message = "Battery health cannot exceed 100%")
    private Double batteryHealth = 100.00;

    private Long pickupLocationId;
}
