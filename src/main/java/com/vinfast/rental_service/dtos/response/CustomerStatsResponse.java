package com.vinfast.rental_service.dtos.response;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
public class CustomerStatsResponse {
    private String period;
    private List<CustomerStatsResponse.TimePeriodStat> stats;

    @Getter
    @Builder
    public static class TimePeriodStat {
        private String period;
        private Long count;
        private BigDecimal percentageChange;
    }

}
