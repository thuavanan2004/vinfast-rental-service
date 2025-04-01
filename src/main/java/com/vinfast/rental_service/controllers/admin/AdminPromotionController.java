package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Promotion management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {

    private final LocationService locationService;

    @Operation(summary = "Create new promotion")
    @PostMapping
    public ResponseData<?> createPromotion(@RequestBody @Valid PromotionRequest request){
        log.info("Create new promotion");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Create new promotion successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create new promotion failed: " + e.getMessage());
        }
    }
}
