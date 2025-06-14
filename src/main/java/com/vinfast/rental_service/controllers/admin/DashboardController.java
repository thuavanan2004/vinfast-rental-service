package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Dashboard")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/statistics")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "get rental order statistics by day, month, and year.")
    @GetMapping("/overview")
    public ResponseData<?> overview(){
        log.info("Fetching overview statistics for dashboard...");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),"Overview retrieved successfully", dashboardService.overview());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Failed to retrieve dashboard overview: " + e.getMessage());
        }
    }

    @Operation(summary = "Get rental order statistics by day, month, and year.")
    @GetMapping("/rental-orders")
    public ResponseData<?> getRentalOrder(@RequestParam(defaultValue = "monthly") String period,
                                          @RequestParam(defaultValue = "auto") String granularity){
        log.info("get rental order statistics by day, month, and year..");
        try{
            if (!List.of("daily", "monthly", "yearly").contains(period.toLowerCase())) {
                throw new IllegalArgumentException("Invalid period parameter. Use daily, monthly or yearly");
            }

            if (!List.of( "second", "minute", "hour", "day", "month", "year").contains(granularity.toLowerCase())) {
                throw new IllegalArgumentException("Invalid period parameter. Use daily, monthly or yearly");
            }

            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get statistics of orders statistics by day, month, and year success",
                    dashboardService.getRentalOrder(period, granularity));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get statistics of orders statistics by day, month, and year failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Get customer statistics by day, month, and year.")
    @GetMapping("/customers")
    public ResponseData<?> getCustomers(@RequestParam(defaultValue = "monthly") String period,
                                        @RequestParam(defaultValue = "auto") String granularity){
        log.info("Get customer statistics by day, month, and year..");
        try{
            if (!List.of("daily", "monthly", "yearly").contains(period.toLowerCase())) {
                throw new IllegalArgumentException("Invalid period parameter. Use daily, monthly or yearly");
            }

            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get customer statistics by day, month, and year..",
                    dashboardService.getCustomerStats(period, granularity));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get customer statistics by day, month, and year.: " + e.getMessage());
        }
    }

    @Operation(summary = "Get cars statistics by day, month, and year.")
    @GetMapping("/cars")
    public ResponseData<?> getCars(@RequestParam(defaultValue = "monthly") String period){
        log.info("Get cars statistics by day, month, and year..");
        try{
            if (!List.of("daily", "monthly", "yearly").contains(period.toLowerCase())) {
                throw new IllegalArgumentException("Invalid period parameter. Use daily, monthly or yearly");
            }

            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get cars statistics by day, month, and year..",
                    dashboardService.getCarsStats(period));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get cars statistics by day, month, and year.: " + e.getMessage());
        }
    }
}
