package com.vinfast.rental_service.repository.projections;

import java.math.BigDecimal;

public interface CarStatsProjection {
    Long getCarModelId();
    String getCarModelName();
    String getCarImage();
    Integer getRentalCount();
    BigDecimal getTotalRevenue();
}

