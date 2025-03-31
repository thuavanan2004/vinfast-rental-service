package com.vinfast.rental_service.dtos.response;

import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.enums.MaintenanceType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceDetailResponse {
    private Long id;
    private String licensePlate;
    private MaintenanceType maintenanceType;
    private String description;
    private String serviceCenter;
    private BigDecimal cost;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer mileage;
    private String notes;
    private LocalDateTime createdAt;
    private CarInfo carInfo;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CarInfo{
        private Long id;
        private String licensePlate;
        private String vinNumber;
        private String color;
        private LocalDate manufacturingDate;
        private int currentMileage;
        private CarStatus status;
        private LocalDate lastMaintenanceDate;
        private Integer nextMaintenanceMileage;
        private BigDecimal batteryHealth;
    }
}
