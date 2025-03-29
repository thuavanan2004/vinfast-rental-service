package com.vinfast.rental_service.model;

import com.vinfast.rental_service.enums.CarModelStatus;
import com.vinfast.rental_service.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 50)
    private String modelCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    @Column(name = "battery_capacity", length = 50)
    private String batteryCapacity;

    @Column(name = "range_per_charge", length = 50)
    private String rangePerCharge;

    @Column(name = "max_speed", length = 50)
    private String maxSpeed;

    @Column(name = "charging_time", length = 50)
    private String chargingTime;

    @Column(name = "seating_capacity")
    private Integer seatingCapacity;

    @Column(name = "motor_power", length = 50)
    private String motorPower;

    @Column(name = "acceleration", length = 50)
    private String acceleration;

    @Column(name = "trunk_capacity", length = 50)
    private String trunkCapacity;

    @Column(name = "weight", length = 50)
    private String weight;

    @Column(name = "length_width_height", length = 100)
    private String lengthWidthHeight;

    @Column(name = "wheelbase", length = 50)
    private String wheelbase;

    @Column(name = "tire_size", length = 50)
    private String tireSize;

    @Column(name = "charging_port_type", length = 50)
    private String chargingPortType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerDay;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerWeek;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerMonth;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePricePerYear;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarModelStatus status = CarModelStatus.available;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "carModel", cascade = CascadeType.ALL)
    private List<CarImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "carModel")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "carModel")
    private List<PromotionApplicableModel> applicablePromotions = new ArrayList<>();
}
