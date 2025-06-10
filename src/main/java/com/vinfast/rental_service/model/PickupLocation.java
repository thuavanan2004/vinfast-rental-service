package com.vinfast.rental_service.model;


import com.vinfast.rental_service.enums.PickupLocationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pickup_locations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String district;

    @Column(name = "contact_phone", nullable = false)
    private String contactPhone;

    private BigDecimal latitude;

    private BigDecimal longitude;

    @Column(name = "operating_hours")
    private String operatingHours;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PickupLocationStatus pickupLocationStatus = PickupLocationStatus.active;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "pickupLocation")
    private List<Car> cars = new ArrayList<>();

    @OneToMany(mappedBy = "pickupLocation")
    private List<RentalOrder> rentalOrders = new ArrayList<>();

}
