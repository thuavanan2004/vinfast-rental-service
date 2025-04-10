package com.vinfast.rental_service.model;

import java.io.Serializable;
import java.util.Objects;

public class OrderInsuranceOptionId implements Serializable {
    private Long order;
    private Long insuranceOption;

    public OrderInsuranceOptionId() {}

    public OrderInsuranceOptionId(Long order, Long insuranceOption) {
        this.order = order;
        this.insuranceOption = insuranceOption;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderInsuranceOptionId that = (OrderInsuranceOptionId) o;
        return Objects.equals(order, that.order) &&
                Objects.equals(insuranceOption, that.insuranceOption);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, insuranceOption);
    }
}
