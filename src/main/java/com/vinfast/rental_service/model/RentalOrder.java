package com.vinfast.rental_service.model;

import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.enums.RentalType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rental_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_code", nullable = false, unique = true)
    private String orderCode;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @Column(name = "actual_return_datetime")
    private LocalDateTime actualReturnDatetime;

    @Enumerated(EnumType.STRING)
    @Column(name = "rental_type", nullable = false)
    private RentalType rentalType;

    @Column(name = "base_price", nullable = false)
    private BigDecimal basePrice;

    @Builder.Default
    @Column(name = "insurance_fee", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal insuranceFee = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "additional_fee", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal additionalFee = BigDecimal.valueOf(0);

    @Builder.Default
    @Column(name = "discount_amount", columnDefinition = "DECIMAL(10,2) DEFAULT 0")
    private BigDecimal discountAmount = BigDecimal.valueOf(0);

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private RentalOrderStatus status = RentalOrderStatus.active;

    @Column(name = "special_requests")
    private String specialRequests;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "order")
    private List<OrderInsuranceOption> insuranceOptions = new ArrayList<>();

    @OneToMany(mappedBy = "order")
        private List<Payment> payments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "pickup_location_id", nullable = false)
    private PickupLocation pickupLocation;

}
