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
    List<CarRentalMost> carRentalMostList;

    @Getter
    @Builder
    public static class CarRentalMost {
        private Integer carId;
        private String carImage;
        private String licensePlate;
        private String carModelName;
        private Long rentalCount;
        private BigDecimal totalRevenue;
    }

}
