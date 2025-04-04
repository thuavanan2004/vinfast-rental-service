package com.vinfast.rental_service.dtos.response;

import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class InsuranceOptionResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal dailyRate;
    private String coverageDetails;
    private InsuranceOptionStatus status;
    private LocalDateTime createdAt;
}
