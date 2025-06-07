package com.vinfast.rental_service.controllers.client;

import com.vinfast.rental_service.dtos.request.CarAvailabilityRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.CarModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Client Car")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/client/cars")
@RequiredArgsConstructor
public class ClientCarController {
    private final CarModelService carModelService;

    @Operation(summary = "Get list car with filter")
    @GetMapping("/available")
    public ResponseData<?> getListCar(@Valid @RequestBody CarAvailabilityRequest request){
        log.info("Get list car for client");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list car successfully", carModelService.getAvailableCarModels(request));
        } catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list car failed");
        }
    }
}
