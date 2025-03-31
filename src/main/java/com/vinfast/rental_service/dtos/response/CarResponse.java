package com.vinfast.rental_service.dtos.response;

import com.vinfast.rental_service.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CarResponse {
    private Long id;
    private String licensePlate;
    private String vinNumber;
    private String color;
    private LocalDate manufacturingDate;
    private int currentMileage;
    private String status;
    private LocalDate lastMaintenanceDate;
    private Integer nextMaintenanceMileage;
    private double batteryHealth;

    private CarModelDto carModel;
    private PickupLocationDto pickupLocation;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class CarModelDto {
        private Long id;
        private String name;
        private String modelCode;
        private VehicleType vehicleType;
        private String batteryCapacity;
        private String rangePerCharge;
        private int seatingCapacity;
        private BigDecimal basePricePerDay;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PickupLocationDto {
        private Long id;
        private String name;
        private String address;
        private String city;
    }
}