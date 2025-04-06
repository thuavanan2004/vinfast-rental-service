package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.response.CarStatsResponse;
import com.vinfast.rental_service.dtos.response.CustomerStatsResponse;
import com.vinfast.rental_service.dtos.response.DashboardOverviewResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderStatsResponse;

import java.util.List;

public interface DashboardService {
    DashboardOverviewResponse overview();

    RentalOrderStatsResponse getRentalOrder(String period, String granularity);

    CustomerStatsResponse getCustomerStats(String period, String granularity);

    CarStatsResponse getCarsStats(String period);
}
