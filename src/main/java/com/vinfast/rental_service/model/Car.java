package com.vinfast.rental_service.model;


import com.vinfast.rental_service.enums.CarStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "license_plate", nullable = false, unique = true)
    private String licensePlate;

    @Column(name = "vin_number", nullable = false, unique = true)
    private String vinNumber;

    @Column(nullable = false)
    private String color;

    @Column(name = "manufacturing_date", nullable = false)
    private LocalDate manufacturingDate;

    @Builder.Default
    @Column(name = "current_mileage", columnDefinition = "INT DEFAULT 0")
    private Integer currentMileage = 0;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private CarStatus status = CarStatus.available;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "next_maintenance_mileage")
    private Integer nextMaintenanceMileage;

    @Builder.Default
    @Column(name = "battery_health", columnDefinition = "DECIMAL(5,2) DEFAULT 100.00")
    private BigDecimal batteryHealth = BigDecimal.valueOf(100.00);

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id")
    private PickupLocation pickupLocation;

    @Builder.Default
    @OneToMany(mappedBy = "car")
    private List<RentalOrder> rentalOrders = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "car")
    private List<MaintenanceLog> maintenanceLogs = new ArrayList<>();

}
