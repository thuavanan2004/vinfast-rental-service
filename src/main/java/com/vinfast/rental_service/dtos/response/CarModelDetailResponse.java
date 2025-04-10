package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarModelDetailResponse {
    private Long id;
    private String name;
    private String modelCode;
    private String vehicleType;
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
    private BigDecimal basePricePerDay;
    private BigDecimal basePricePerWeek;
    private BigDecimal basePricePerMonth;
    private BigDecimal basePricePerYear;
    private String description;
    private String status;
    private List<String> imageUrls;
}

