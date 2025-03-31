package com.vinfast.rental_service.dtos.response;

import com.vinfast.rental_service.enums.MaintenanceType;
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
public class MaintenanceResponse {
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
}
