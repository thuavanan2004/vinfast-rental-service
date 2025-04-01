package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.PromotionResponse;
import com.vinfast.rental_service.enums.PromotionStatus;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.PromotionMapper;
import com.vinfast.rental_service.model.Promotion;
import com.vinfast.rental_service.repository.PromotionRepository;
import com.vinfast.rental_service.service.PromotionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;

    private final PromotionMapper promotionMapper;

    @Override
    public void createPromotion(PromotionRequest request) {
        Promotion promotion = promotionMapper.toEntity(request);

        promotionRepository.save(promotion);

        log.info("Create promotion successfully");
    }

    @Override
    public PageResponse<?> getAll(PromotionStatus status, Pageable pageable) {
        Page<Promotion> promotions;
        if(status != null){
            promotions = promotionRepository.findByStatus(status, pageable);
        } else {
            promotions = promotionRepository.findAll(pageable);
        }

        List<PromotionResponse> list = promotions.stream().map(promotionMapper::toDTO).toList();

        return PageResponse.builder()
                .page(promotions.getNumber())
                .size(promotions.getSize())
                .totalPage(promotions.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public void updatePromotion(long promotionId, PromotionRequest request) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));

        promotionMapper.updatePromotion(request, promotion);

        promotionRepository.save(promotion);

        log.info("Update promotion successfully");
    }

    @Override
    public PromotionResponse getPromotionById(long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(() -> new ResourceNotFoundException("Promotion not found"));
        return promotionMapper.toDTO(promotion);
    }

}
