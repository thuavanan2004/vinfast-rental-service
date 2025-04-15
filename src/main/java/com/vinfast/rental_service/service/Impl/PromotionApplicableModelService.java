package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.model.Promotion;
import com.vinfast.rental_service.model.PromotionApplicableModel;
import com.vinfast.rental_service.repository.PromotionApplicableModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PromotionApplicableModelService {
    private final PromotionApplicableModelRepository promotionApplicableModelRepository;

    @Transactional
    public void create(Promotion promotion, CarModel carModel){
        PromotionApplicableModel promotionApplicableModel = PromotionApplicableModel.builder()
                .promotion(promotion)
                .carModel(carModel)
                .build();
        promotionApplicableModelRepository.save(promotionApplicableModel);
    }
}
