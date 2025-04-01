package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.InsuranceOptionRequest;
import com.vinfast.rental_service.dtos.response.InsuranceOptionResponse;
import com.vinfast.rental_service.model.InsuranceOption;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface InsuranceOptionMapper {
    InsuranceOptionResponse toDTO(InsuranceOption entity);

    InsuranceOption toEntity(InsuranceOptionRequest request);

    void updateInsuranceOption(InsuranceOptionRequest request, @MappingTarget InsuranceOption insuranceOption);
}
