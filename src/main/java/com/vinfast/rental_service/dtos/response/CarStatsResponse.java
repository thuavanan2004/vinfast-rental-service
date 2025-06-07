package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CarStatsResponse {
    private String period;
    private List<CarRentalMost> carRentalMostList;

    @Getter
    @Builder
    public static class CarRentalMost {
        private Long carModelId;
        private String carModelName;
        private String carImage;
        private Integer rentalCount;
        private BigDecimal totalRevenue;
    }
}

