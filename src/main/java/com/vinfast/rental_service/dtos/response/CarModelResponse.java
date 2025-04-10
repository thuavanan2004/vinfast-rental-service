package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CarModelResponse {
    private Long id;
    private String name;
    private String modelCode;
    private String mainImage;
    private String vehicleType;
    private String rangePerCharge;
    private String trunkCapacity;
    private Integer seatingCapacity;
    private BigDecimal basePrice;
    private Boolean available;
}
