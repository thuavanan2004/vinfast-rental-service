package com.vinfast.rental_service.controllers.client;


import com.vinfast.rental_service.dtos.request.RentalOrderCreateRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.RentalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Client Booking")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/client/bookings")
@RequiredArgsConstructor
public class ClientBookingController {
    private final RentalOrderService rentalOrderService;

    @Operation(summary = "Create a car rental order")
    @PostMapping
    public ResponseData<?> createOrder(@Valid @RequestBody RentalOrderCreateRequest request){
        log.info("Create a car rental order");
        try{
            rentalOrderService.createOrder(request);
            return new ResponseData<>(HttpStatus.OK.value(),"Create a car rental order successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create a car rental order failed");
        }
    }

    @Operation(summary = "Get list rental order by userId")
    @GetMapping("/my-bookings/{userId}")
    public ResponseData<?> getOrders(@PathVariable @Min(1) long userId, Pageable pageable){
        log.info("Get list rental order by userId");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),"Get list rental order by userId successfully", rentalOrderService.getOrders(userId, pageable));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list rental order by userId failed");
        }
    }

    @Operation(summary = "Get info rental order")
    @GetMapping("/{rentalOrderId}")
    public ResponseData<?> getDetailOrder(@PathVariable @Min(1) long rentalOrderId){
        log.info("Get info rental order");
        try{
            return new ResponseData<>(HttpStatus.OK.value(),"Get info rental order successfully", rentalOrderService.getDetailOrder(rentalOrderId));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get info rental order failed");
        }
    }
}
