package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.CustomerStatsResponse;
import com.vinfast.rental_service.dtos.response.DashboardOverviewResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderStatsResponse;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.repository.RentalOrderRepository;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final RentalOrderRepository rentalOrderRepository;

    private final UserRepository userRepository;

    @Override
    public DashboardOverviewResponse overview() {
        long totalOrders = rentalOrderRepository.count();
        double totalRevenue = rentalOrderRepository.calculateTotalRevenue();
        long pendingOrders = rentalOrderRepository.countByStatus(RentalOrderStatus.pending);
        long completedOrders = rentalOrderRepository.countByStatus(RentalOrderStatus.completed);

        return new DashboardOverviewResponse(totalOrders, totalRevenue, pendingOrders, completedOrders);
    }

    @Override
    public RentalOrderStatsResponse getRentalOrder(String period) {
        PeriodInfo periodInfo = resolvePeriod(period);

        List<Object[]> currentStats = rentalOrderRepository.findPeriodStats(
                periodInfo.startDate(),
                LocalDateTime.now(),
                periodInfo.dateFormat()
        );

        List<Object[]> previousStats = rentalOrderRepository.findPeriodStats(
                periodInfo.startDate().minus(periodInfo.period(), periodInfo.unit()),
                periodInfo.startDate(),
                periodInfo.dateFormat()
        );

        return buildResponse(currentStats, previousStats, period);
    }

    @Override
    public CustomerStatsResponse getCustomerStats(String period) {
        PeriodInfo periodInfo = resolvePeriod(period);

        List<Object[]> currentStats = userRepository.findPeriodStats(
                periodInfo.startDate(),
                LocalDateTime.now(),
                periodInfo.dateFormat()
        );

        List<Object[]> previousStats = userRepository.findPeriodStats(
                periodInfo.startDate().minus(periodInfo.period, periodInfo.unit),
                periodInfo.startDate(),
                periodInfo.dateFormat()
        );
        return buildCustomerResponse(currentStats, previousStats, period);
    }

    private record PeriodInfo(
            LocalDateTime startDate,
            ChronoUnit unit,
            int period,
            String dateFormat
    ) {}

    private PeriodInfo resolvePeriod(String period) {
        return switch (period.toLowerCase()) {
            case "daily" -> new PeriodInfo(
                    LocalDateTime.now().minusDays(1),
                    ChronoUnit.DAYS,
                    1,
                    "%Y-%m-%d"
            );
            case "monthly" -> new PeriodInfo(
                    LocalDateTime.now().minusMonths(1),
                    ChronoUnit.MONTHS,
                    1,
                    "%Y-%m"
            );
            case "yearly" -> new PeriodInfo(
                    LocalDateTime.now().minusYears(1),
                    ChronoUnit.YEARS,
                    1,
                    "%Y"
            );
            default -> throw new IllegalArgumentException("Invalid period");
        };
    }
    private RentalOrderStatsResponse buildResponse(List<Object[]> current,
                                                   List<Object[]> previous,
                                                   String period) {
        Map<String, BigDecimal[]> currentMap = mapToPeriodStats(current);
        Map<String, BigDecimal[]> previousMap = mapToPeriodStats(previous);

        List<RentalOrderStatsResponse.TimePeriodStat> stats = currentMap.entrySet().stream()
                .map(entry -> {
                    String time = entry.getKey();
                    BigDecimal currentRevenue = entry.getValue()[0];
                    BigDecimal currentCount = entry.getValue()[1];
                    BigDecimal[] previousValues = previousMap.getOrDefault(time, new BigDecimal[2]);

                    return RentalOrderStatsResponse.TimePeriodStat.builder()
                            .period(time)
                            .totalRevenue(calculateRevenueStat(currentRevenue, previousValues[0]))
                            .orderCount(calculateOrderStat(currentCount, previousValues[1]))
                            .build();
                })
                .collect(Collectors.toList());

        return RentalOrderStatsResponse.builder()
                .period(period)
                .stats(stats)
                .build();
    }

    private CustomerStatsResponse buildCustomerResponse(List<Object[]> current,
                                                        List<Object[]> previous,
                                                        String period){
        Map<String, BigDecimal[]> currentMap = mapCustomerToPeriodStats(current);
        Map<String, BigDecimal[]> previousMap = mapCustomerToPeriodStats(previous);

        List<CustomerStatsResponse.TimePeriodStat> stats = currentMap.entrySet().stream().map(entry -> {
            String time = entry.getKey();
            BigDecimal currentCount = entry.getValue()[0];
            BigDecimal[] previousCount = previousMap.getOrDefault(time, new BigDecimal[2]);

            return CustomerStatsResponse.TimePeriodStat.builder()
                    .period(time)
                    .count(currentCount.longValue())
                    .percentageChange(calculatePercentageChange(previousCount[0], currentCount))
                    .build();

        }).collect(Collectors.toList());

        return CustomerStatsResponse.builder()
                .period(period)
                .stats(stats)
                .build();
    }


    private RentalOrderStatsResponse.RevenueStat calculateRevenueStat(BigDecimal current, BigDecimal previous) {
        BigDecimal percentage = calculatePercentageChange(previous, current);
        return RentalOrderStatsResponse.RevenueStat.builder()
                .amount(current.setScale(2, RoundingMode.HALF_UP))
                .percentageChange(percentage)
                .build();
    }

    private RentalOrderStatsResponse.OrderStat calculateOrderStat(BigDecimal current, BigDecimal previous) {
        Long count = current.longValue();
        BigDecimal percentage = calculatePercentageChange(previous, current);

        return RentalOrderStatsResponse.OrderStat.builder()
                .count(count)
                .percentageChange(percentage)
                .build();
    }

    private BigDecimal calculatePercentageChange(BigDecimal previous, BigDecimal current) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return current.compareTo(BigDecimal.ZERO) == 0
                    ? BigDecimal.ZERO
                    : new BigDecimal(100);
        }
        return current.subtract(previous)
                .divide(previous, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private Map<String, BigDecimal[]> mapToPeriodStats(List<Object[]> stats) {
        return stats.stream().filter(Objects::nonNull).collect(Collectors.toMap(
                row -> row[0] != null ? row[0].toString() : "N/A",
                row -> new BigDecimal[] {
                        row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO,
                        row[2] != null ? new BigDecimal(row[2].toString()) : BigDecimal.ZERO
                }
        ));
    }

    private Map<String, BigDecimal[]> mapCustomerToPeriodStats(List<Object[]> stats) {
        return stats.stream().filter(Objects::nonNull).collect(Collectors.toMap(
                row -> row[0] != null ? row[0].toString() : "N/A",
                row -> new BigDecimal[] {
                        row[1] != null ? new BigDecimal(row[1].toString()) : BigDecimal.ZERO
                }
        ));
    }
}
