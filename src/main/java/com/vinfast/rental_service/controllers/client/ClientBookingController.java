package com.vinfast.rental_service.controllers.client;


import com.vinfast.rental_service.dtos.request.RentalOrderCreateRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.RentalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Booking")
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
}
