package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.DashboardService;
import com.vinfast.rental_service.service.SupportTicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @Operation(summary = "Get an overview of total orders, revenue, pending and completed orders.")
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

    @Operation(summary = "Get statistics of orders based on their status.")
    @GetMapping("/orders")
    public ResponseData<?> orders(){
        log.info("Get statistics of orders based on their status.");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get statistics of orders based on their status."
            );
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get statistics of orders based on their status: " + e.getMessage());
        }
    }

    @Operation(summary = "Get revenue statistics by day, month, and year.")
    @GetMapping("/revenue")
    public ResponseData<?> revenue(){
        log.info("Get revenue statistics by day, month, and year..");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get revenue statistics by day, month, and year.."
            );
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get revenue statistics by day, month, and year.: " + e.getMessage());
        }
    }
}
