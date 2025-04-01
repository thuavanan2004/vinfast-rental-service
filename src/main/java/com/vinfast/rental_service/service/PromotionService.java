package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.PromotionRequest;

public interface PromotionService {
    void createPromotion(PromotionRequest request);
}
