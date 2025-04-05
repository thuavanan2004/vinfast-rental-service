package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class RentalOrderStatsResponse {
    private String period;
    private List<TimePeriodStat> stats;

    @Getter
    @Builder
    public static class TimePeriodStat {
        private String period;
        private RevenueStat totalRevenue;
        private OrderStat orderCount;
    }

    @Getter
    @Builder
    public static class RevenueStat {
        private BigDecimal amount;
        private BigDecimal percentageChange;
    }

    @Getter
    @Builder
    public static class OrderStat {
        private Long count;
        private BigDecimal percentageChange;
    }
}
