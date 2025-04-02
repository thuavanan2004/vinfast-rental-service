package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.DashboardOverviewResponse;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.repository.RentalOrderRepository;
import com.vinfast.rental_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final RentalOrderRepository rentalOrderRepository;

    @Override
    public DashboardOverviewResponse overview() {
        long totalOrders = rentalOrderRepository.count();
        double totalRevenue = rentalOrderRepository.calculateTotalRevenue();
        long pendingOrders = rentalOrderRepository.countByStatus(RentalOrderStatus.pending);
        long completedOrders = rentalOrderRepository.countByStatus(RentalOrderStatus.completed);

        return new DashboardOverviewResponse(totalOrders, totalRevenue, pendingOrders, completedOrders);
    }
}
