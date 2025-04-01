package com.vinfast.rental_service.model;

import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "insurance_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InsuranceOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "daily_rate", nullable = false)
    private BigDecimal dailyRate;

    @Column(name = "coverage_details")
    private String coverageDetails;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private InsuranceOptionStatus status = InsuranceOptionStatus.active;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @OneToMany(mappedBy = "insuranceOption")
    private List<OrderInsuranceOption> orderOptions = new ArrayList<>();

}
