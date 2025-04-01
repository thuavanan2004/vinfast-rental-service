package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.dtos.response.PromotionResponse;
import com.vinfast.rental_service.model.Promotion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PromotionMapper {
    Promotion toEntity(PromotionRequest request);

    PromotionResponse toDTO(Promotion entity);

    void updatePromotion(PromotionRequest request, @MappingTarget Promotion promotion);
}
