package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.CarStatsResponse;
import com.vinfast.rental_service.dtos.response.CustomerStatsResponse;
import com.vinfast.rental_service.dtos.response.DashboardOverviewResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderStatsResponse;
import com.vinfast.rental_service.enums.DateFormatPattern;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.enums.TimeGranularity;
import com.vinfast.rental_service.exceptions.InvalidPeriodException;
import com.vinfast.rental_service.repository.CarRepository;
import com.vinfast.rental_service.repository.RentalOrderRepository;
import com.vinfast.rental_service.repository.UserRepository;
import com.vinfast.rental_service.repository.projections.CarStatsProjection;
import com.vinfast.rental_service.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;



@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final RentalOrderRepository rentalOrderRepository;

    private final CarRepository carRepository;

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
    public RentalOrderStatsResponse getRentalOrder(String period, String granularity) {
        TimeGranularity granularityEnum = parseGranularity(granularity);
        PeriodInfo periodInfo = resolvePeriod(period, granularityEnum);

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
    public CustomerStatsResponse getCustomerStats(String period, String granularity) {
        TimeGranularity timeGranularity = parseGranularity(granularity);
        PeriodInfo periodInfo = resolvePeriod(period, timeGranularity);

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

    @Override
    public CarStatsResponse getCarsStats(String period) {
        PeriodInfo periodInfo = resolvePeriod(period, TimeGranularity.AUTO);

        List<CarStatsProjection> projections = carRepository.findPeriodStats(
                periodInfo.startDate(),
                LocalDateTime.now(),
                periodInfo.dateFormat()
        );

        List<CarStatsResponse.CarRentalMost> list = projections.stream()
                .map(p -> CarStatsResponse.CarRentalMost.builder()
                        .carId(p.getCarId())
                        .licensePlate(p.getLicensePlate())
                        .carImage(p.getCarImage())
                        .carModelName(p.getCarModelName())
                        .rentalCount(p.getRentalCount())
                        .totalRevenue(p.getTotalRevenue())
                        .build()).collect(Collectors.toList());

        return CarStatsResponse.builder()
                .period(period)
                .carRentalMostList(list)
                .build();
    }

    private record PeriodInfo(
            LocalDateTime startDate,
            ChronoUnit unit,
            int period,
            String dateFormat
    ) {}

    private TimeGranularity parseGranularity(String granularity) {
        try {
            return TimeGranularity.valueOf(granularity.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TimeGranularity.AUTO;
        }
    }

    private PeriodInfo resolvePeriod(String period, TimeGranularity granularity) {
        return switch (period.toLowerCase()) {
            case "daily" -> resolvePeriodConfig(granularity, ChronoUnit.DAYS, DateFormatPattern.DAILY);
            case "monthly" -> resolvePeriodConfig(granularity, ChronoUnit.MONTHS, DateFormatPattern.MONTHLY);
            case "yearly" -> resolvePeriodConfig(granularity, ChronoUnit.YEARS, DateFormatPattern.YEARLY);
            default -> throw new InvalidPeriodException("Not valid: " + period);
        };
    }

    private PeriodInfo resolvePeriodConfig(TimeGranularity granularity, ChronoUnit unit, DateFormatPattern defaultFormat) {
        String dateFormat = switch (granularity) {
            case DAY -> "%Y-%m-%d";
            case HOUR -> "%Y-%m-%d %H";
            case MINUTE -> "%Y-%m-%d %H:%i";
            case SECOND -> "%Y-%m-%d %H:%i:%S";
            default -> defaultFormat.getPattern();
        };

        return new PeriodInfo(
                LocalDateTime.now().minus(1, unit),
                unit,
                1,
                dateFormat
        );
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
                row -> safeToString(row[0]),
                row -> new BigDecimal[] {
                        safeToBigDecimal(row[1]),
                        safeToBigDecimal(row[2])
                }
        ));
    }

    private Map<String, BigDecimal[]> mapCustomerToPeriodStats(List<Object[]> stats) {
        return stats.stream().filter(Objects::nonNull).collect(Collectors.toMap(
                row -> safeToString(row[0]),
                row -> new BigDecimal[] {safeToBigDecimal(row[1])}
        ));
    }

    private String safeToString(Object obj){
        return obj != null ? obj.toString() : "N/A";
    }

    private BigDecimal safeToBigDecimal(Object obj) {
        try {
            return new BigDecimal(obj.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
