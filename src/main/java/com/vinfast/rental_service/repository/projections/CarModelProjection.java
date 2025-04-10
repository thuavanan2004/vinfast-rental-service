package com.vinfast.rental_service.repository.projections;

import com.vinfast.rental_service.enums.CarModelStatus;

import java.math.BigDecimal;

public interface CarModelProjection {
    Long getId();
    String getName();
    String getModelCode();
    String getMainImage();
    String getVehicleType();
    String getRangePerCharge();
    String getTrunkCapacity();
    Integer getSeatingCapacity();
    BigDecimal getBasePrice();
    Integer getAvailable();
}
