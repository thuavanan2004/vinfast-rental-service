package com.vinfast.rental_service.service.validateService;

import com.vinfast.rental_service.dtos.request.RentalOrderCreateRequest;
import com.vinfast.rental_service.exceptions.InvalidDateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderValidationService {
    public void validateOrderRequest(RentalOrderCreateRequest request) {
        LocalDateTime now = LocalDateTime.now();

        if (!request.getStartDateTime().isAfter(now)) {
            throw new InvalidDateException("start_date_invalid", "Start date must be in the future.");
        }
        if (!request.getStartDateTime().isBefore(request.getEndDateTime())) {
            throw new InvalidDateException("end_date_invalid", "Start date must be before end date.");
        }
    }
}

