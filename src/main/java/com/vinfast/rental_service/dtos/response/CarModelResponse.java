package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class CarModelResponse {
    private Long id;
    private String name;
    private String modelCode;
    private Long brandId;
    private String vehicleType;
    private BigDecimal batteryCapacity;
    private Integer rangePerCharge;
    private Integer maxSpeed;
    private BigDecimal chargingTime;
    private Integer seatingCapacity;
    private Integer motorPower;
    private BigDecimal acceleration;
    private Integer trunkCapacity;
    private Integer weightKg;
    private Integer lengthMm;
    private Integer widthMm;
    private Integer heightMm;
    private Integer wheelbaseMm;
    private String tireSize;
    private String chargingPortType;
    private BigDecimal basePricePerDay;
    private BigDecimal basePricePerWeek;
    private BigDecimal basePricePerMonth;
    private BigDecimal basePricePerYear;
    private String description;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
}
