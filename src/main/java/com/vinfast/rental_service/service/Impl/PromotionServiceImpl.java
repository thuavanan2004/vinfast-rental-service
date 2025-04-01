package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.repository.PromotionRepository;
import com.vinfast.rental_service.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    @Override
    public void createPromotion(PromotionRequest request) {

    }
}
