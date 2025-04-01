package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.dtos.request.PromotionRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.PromotionStatus;
import com.vinfast.rental_service.service.LocationService;
import com.vinfast.rental_service.service.PromotionService;
import com.vinfast.rental_service.validate.EnumPattern;
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

@Tag(name = "Promotion management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/promotions")
@RequiredArgsConstructor
public class AdminPromotionController {

    private final PromotionService promotionService;

    @Operation(summary = "Create new promotion")
    @PostMapping
    public ResponseData<?> createPromotion(@RequestBody @Valid PromotionRequest request){
        log.info("Create new promotion");
        try{
            promotionService.createPromotion(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create new promotion successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create new promotion failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Get list promotion")
    @GetMapping
    public ResponseData<?> getAll(@RequestParam(required = false) @EnumPattern(name="status", regexp = "active|inactive|expired") PromotionStatus status,
                                  Pageable pageable){
        log.info("Get list promotion");
        try{

            return new ResponseData<>(HttpStatus.OK.value(), "Get list promotion successfully", promotionService.getAll(status, pageable));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list promotion failed: " + e.getMessage());
        }
    }

    @Operation(summary = "Update promotion")
    @PatchMapping("/{promotionId}")
    public ResponseData<?> updatePromotion(@PathVariable @Min(1) long promotionId, @RequestBody @Valid PromotionRequest request){
        log.info("Update promotion");
        try{
            promotionService.updatePromotion(promotionId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update promotion successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update promotion failed: " + e.getMessage());
        }
    }
}
