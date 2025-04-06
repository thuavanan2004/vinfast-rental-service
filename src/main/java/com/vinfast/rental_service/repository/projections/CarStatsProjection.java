package com.vinfast.rental_service.repository.projections;

import java.math.BigDecimal;

public interface CarStatsProjection {
    Integer getCarId();
    String getLicensePlate();
    String getCarImage();
    String getCarModelName();
    Long getRentalCount();
    BigDecimal getTotalRevenue();
}
