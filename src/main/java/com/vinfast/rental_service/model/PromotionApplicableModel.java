package com.vinfast.rental_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;


@Entity
@Table(name = "promotion_applicable_models")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PromotionApplicableModelId.class)
public class PromotionApplicableModel {

    @Id
    @ManyToOne
    @JoinColumn(name = "promotion_id", nullable = false)
    private Promotion promotion;

    @Id
    @ManyToOne
    @JoinColumn(name = "car_model_id", nullable = false)
    private CarModel carModel;
}

