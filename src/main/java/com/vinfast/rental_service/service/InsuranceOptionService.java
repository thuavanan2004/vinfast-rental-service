package com.vinfast.rental_service.service;

import com.vinfast.rental_service.dtos.request.InsuranceOptionRequest;
import com.vinfast.rental_service.dtos.response.InsuranceOptionResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import org.springframework.data.domain.Pageable;

public interface InsuranceOptionService {
    PageResponse<?> getAll(InsuranceOptionStatus status, Pageable pageable);

    InsuranceOptionResponse getInsuranceOptionById(long id);

    void createInsuranceOption(InsuranceOptionRequest request);

    void updateInsuranceOption(long id, InsuranceOptionRequest request);

    void deleteInsuranceOption(long id);
}
