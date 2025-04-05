package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.response.DashboardOverviewResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderStatsResponse;

public interface DashboardService {
    DashboardOverviewResponse overview();

    RentalOrderStatsResponse getRentalOrder(String period);
}
