package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.CarModelStatus;
import com.vinfast.rental_service.enums.VehicleType;
import com.vinfast.rental_service.validate.EnumPattern;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class CarModelUpdateRequest {
    @NotBlank(message = "Model name cannot be blank")
    @Size(max = 100, message = "Model name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Model code cannot be blank")
    @Size(max = 50, message = "Model code cannot exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "Model code must be uppercase alphanumeric with hyphens")
    private String modelCode;

    @NotNull(message = "Vehicle type cannot be null")
    @EnumPattern(name="vehicleType", regexp = "sedan|suv|hatchback|mpv")
    private VehicleType vehicleType;

    private String batteryCapacity;
    private String rangePerCharge;
    private String maxSpeed;
    private String chargingTime;
    private Integer seatingCapacity;
    private String motorPower;
    private String acceleration;
    private String trunkCapacity;
    private String weight;
    private String lengthWidthHeight;
    private String wheelbase;
    private String tireSize;
    private String chargingPortType;

    @NotNull(message = "Daily price cannot be null")
    @Positive(message = "Daily price must be positive")
    private BigDecimal basePricePerDay;

    @NotNull(message = "Weekly price cannot be null")
    @Positive(message = "Weekly price must be positive")
    private BigDecimal basePricePerWeek;

    @NotNull(message = "Monthly price cannot be null")
    @Positive(message = "Monthly price must be positive")
    private BigDecimal basePricePerMonth;

    @NotNull(message = "Yearly price cannot be null")
    @Positive(message = "Yearly price must be positive")
    private BigDecimal basePricePerYear;

    private String description;

    @EnumPattern(name = "status", regexp = "available|coming_soon|discontinued")
    private CarModelStatus status;
}
