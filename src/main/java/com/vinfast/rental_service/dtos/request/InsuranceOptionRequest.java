package com.vinfast.rental_service.dtos.request;

import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class InsuranceOptionRequest {
    private String name;
    private String description;
    private BigDecimal dailyRate;
    private String coverageDetails;
    private InsuranceOptionStatus status;
}
