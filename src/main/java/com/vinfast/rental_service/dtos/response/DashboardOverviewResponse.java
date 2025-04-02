package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DashboardOverviewResponse {
    private long totalOrders;
    private double totalRevenue;
    private long pendingOrders;
    private long completedOrders;
}
