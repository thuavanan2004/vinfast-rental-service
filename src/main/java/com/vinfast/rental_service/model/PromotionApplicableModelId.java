package com.vinfast.rental_service.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

public class PromotionApplicableModelId implements Serializable {
    private Long promotion;
    private Long carModel;

    public PromotionApplicableModelId(){}

    public PromotionApplicableModelId(Long promotion, Long carModel){
        this.promotion = promotion;
        this.carModel = carModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PromotionApplicableModelId that = (PromotionApplicableModelId) o;
        return Objects.equals(promotion, that.promotion) &&
                Objects.equals(carModel, that.carModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(promotion, carModel);
    }
}
