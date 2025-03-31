package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.RentalOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
}
