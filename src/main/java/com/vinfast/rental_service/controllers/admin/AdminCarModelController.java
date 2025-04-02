package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelUpdateRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.CarModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Car model management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/car-models")
@RequiredArgsConstructor
public class AdminCarModelController {

    private final CarModelService carModelService;

    @PreAuthorize("hasAuthority('car_model:create')")
    @Operation(summary = "Create car-model")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseData<?> createCarModel(@Valid @ModelAttribute CarModelCreateRequest request){
        log.info("Create car-model");
        try{
            if (!request.isValidPrimaryImageIndex()) {
                throw new IllegalArgumentException("Primary image index must be between 0 and " + (request.getImages().size() - 1));
            }
            carModelService.createCarModel(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create car-model successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create car-model failed");
        }
    }

    @PreAuthorize("hasAuthority('car_model:update')")
    @Operation(summary = "Update car-model")
    @PutMapping("/update/{carModelId}")
    public ResponseData<?> updateCarModel(@PathVariable long carModelId, @Valid @RequestBody CarModelUpdateRequest request){
        log.info("Update car-model");
        try{

            carModelService.updateCarModel(carModelId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update car-model successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update car-model failed");
        }
    }

}
