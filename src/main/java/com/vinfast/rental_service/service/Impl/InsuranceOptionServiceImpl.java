package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.InsuranceOptionRequest;
import com.vinfast.rental_service.dtos.response.InsuranceOptionResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.InsuranceOptionMapper;
import com.vinfast.rental_service.model.InsuranceOption;
import com.vinfast.rental_service.repository.InsuranceOptionRepository;
import com.vinfast.rental_service.service.InsuranceOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InsuranceOptionServiceImpl implements InsuranceOptionService {
    private final InsuranceOptionRepository insuranceOptionRepository;

    private final InsuranceOptionMapper insuranceOptionMapper;

    @Override
    public PageResponse<?> getAll(InsuranceOptionStatus status, Pageable pageable) {
        Page<InsuranceOption> records;
        if(status != null){
            records = insuranceOptionRepository.findByStatus(status, pageable);
        }else {
            records = insuranceOptionRepository.findAll(pageable);
        }

        List<InsuranceOptionResponse> list = records.stream().map(insuranceOptionMapper::toDTO).toList();
        return PageResponse.builder()
                .page(records.getNumber())
                .size(records.getSize())
                .totalPage(records.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public InsuranceOptionResponse getInsuranceOptionById(long id) {
        InsuranceOption insuranceOption = insuranceOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance option not found"));

        return insuranceOptionMapper.toDTO(insuranceOption);
    }

    @Override
    public void createInsuranceOption(InsuranceOptionRequest request) {
        InsuranceOption insuranceOption = insuranceOptionMapper.toEntity(request);

        insuranceOptionRepository.save(insuranceOption);

        log.info("Create insurance option successfully!");
    }

    @Override
    public void updateInsuranceOption(long id, InsuranceOptionRequest request) {
        InsuranceOption insuranceOption = insuranceOptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance option not found"));

        insuranceOptionMapper.updateInsuranceOption(request, insuranceOption);

        insuranceOptionRepository.save(insuranceOption);
        log.info("Update insurance option successfully");
    }

    @Override
    public void deleteInsuranceOption(long id) {
        insuranceOptionRepository.deleteById(id);

        log.info("Delete insurance option successfully");
    }
}
