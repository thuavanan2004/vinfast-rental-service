package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.enums.PromotionStatus;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    void createPromotion(PromotionRequest request);

    PageResponse<?> getAll(PromotionStatus status, Pageable pageable);

    void updatePromotion(long promotionId, PromotionRequest request);
}
