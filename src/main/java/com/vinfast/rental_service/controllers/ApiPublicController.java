package com.vinfast.rental_service.controllers;

import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import com.vinfast.rental_service.service.CarModelService;
import com.vinfast.rental_service.service.InsuranceOptionService;
import com.vinfast.rental_service.service.LocationService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Api public")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public")
public class ApiPublicController {

    private final CarModelService carModelService;

    private final LocationService locationService;

    private final InsuranceOptionService insuranceOptionService;

    @Operation(summary = "Get list car-model")
    @GetMapping("/car-models")
    public ResponseData<?> getListCarModel(){
        log.info("Get list car-model");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list car-model successfully", carModelService.getListCarModel());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list car-model failed");
        }
    }

    @Operation(summary = "Get car-model")
    @GetMapping("/car-models/{carModelId}")
    public ResponseData<?> getCarModel(@PathVariable long carModelId){
        log.info("Get car-model");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get car-model by id successfully", carModelService.getCarModel(carModelId));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get car-model by id failed");
        }
    }

    @Operation(summary = "Get list location")
    @GetMapping("/locations")
    public ResponseData<?> getListLocation(){
        log.info("Get list location");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list location successfully", locationService.getListLocation());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list location failed");
        }
    }

    @Operation(summary = "Get list insurance option")
    @GetMapping("/insurance-options")
    public ResponseData<?> getAll(@RequestParam(required = false) @EnumPattern(name="status", regexp = "active|inactive") InsuranceOptionStatus status,
                                  Pageable pageable){
        log.info("Get list insurance option");
        try{

            return new ResponseData<>(HttpStatus.OK.value(), "Get list insurance option successfully", insuranceOptionService.getAll(status, pageable));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list insurance option failed: " + e.getMessage());
        }
    }
}
