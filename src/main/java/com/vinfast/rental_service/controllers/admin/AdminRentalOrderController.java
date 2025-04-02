package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.request.SpecialRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.service.RentalOrderService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Rental orders management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/rental-orders")
@RequiredArgsConstructor
public class AdminRentalOrderController {

    private final RentalOrderService rentalOrderService;

    @PreAuthorize("hasAuthority('rental_order:read')")
    @Operation(summary = "Get list rental orders")
    @GetMapping
    public ResponseData<?> getAll(Pageable pageable,
                                  @RequestParam(required = false) String[] orders,
                                  @RequestParam(required = false) String[] cars){
        log.info("Get list rental orders");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),
                    "Get list rental orders successfully",
                    rentalOrderService.getAll(pageable, orders, cars));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list rental orders failed");
        }
    }

    @PreAuthorize("hasAuthority('rental_order:update')")
    @Operation(summary = "Update rental order status")
    @PatchMapping("/{orderId}/status")
    public ResponseData<?> updateOrderStatus(
            @PathVariable @Min(1) long orderId,
            @RequestParam @EnumPattern(name="status", regexp = "pending|confirmed|active|completed|cancelled") RentalOrderStatus status) {

        log.info("Updating status for order ID: {}", orderId);
        try {
            rentalOrderService.updateRentalOrderStatus(orderId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Order status updated successfully");
        } catch (Exception e) {
            log.error("Error updating order status: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Failed to update order status: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('rental_order:create')")
    @Operation(summary = "Add special request for order")
    @PostMapping("/{orderId}/special-request")
    public ResponseData<?> addSpecialRequest(
            @PathVariable @Min(1) long orderId,
            @RequestBody @Valid SpecialRequest request) {

        log.info("Add special request for order: {}", orderId);
        try {
            rentalOrderService.addSpecialRequest(orderId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Add special request for order successfully");
        } catch (Exception e) {
            log.error("Error Add special request for order: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Add special request failed: " + e.getMessage());
        }
    }
}
