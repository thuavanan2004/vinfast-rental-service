package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.RentalOrderCreateRequest;
import com.vinfast.rental_service.dtos.request.SpecialRequest;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderResponse;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import org.springframework.data.domain.Pageable;

public interface RentalOrderService {
    PageResponse<?> getAll(Pageable pageable, String[] orders, String[] cars);

    void updateRentalOrderStatus(long orderId, RentalOrderStatus status);

    void addSpecialRequest(long orderId, SpecialRequest request);

    void createOrder(RentalOrderCreateRequest request);

    PageResponse<?> getOrders(long userId, Pageable pageable);

    RentalOrderResponse getDetailOrder(long rentalOrderId);
}
