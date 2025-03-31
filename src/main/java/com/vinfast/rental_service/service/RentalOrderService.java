package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.response.PageResponse;
import org.springframework.data.domain.Pageable;

public interface RentalOrderService {
    PageResponse<?> getAll(Pageable pageable, String[] orders, String[] cars);
}
