package com.vinfast.rental_service.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_insurance_options")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(OrderInsuranceOptionId.class)
public class OrderInsuranceOption {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private RentalOrder order;

    @Id
    @ManyToOne
    @JoinColumn(name = "insurance_option_id", nullable = false)
    private InsuranceOption insuranceOption;

    @Column(nullable = false)
    private BigDecimal fee;
}
