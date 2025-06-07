package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.InsuranceOptionRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.service.InsuranceOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Insurance option management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/insurance-options")
@RequiredArgsConstructor
public class AdminInsuranceOptionController {

    private final InsuranceOptionService insuranceOptionService;

    @PreAuthorize("hasAuthority('insurance:read')")
    @Operation(summary = "Get insurance option by id")
    @GetMapping("/{id}")
    public ResponseData<?> getInsuranceOptionById(@PathVariable @Min(1) long id){
        log.info("Get insurance option by id");
        try{

            return new ResponseData<>(HttpStatus.OK.value(), "Get insurance option by id successfully", insuranceOptionService.getInsuranceOptionById(id));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get insurance option by id failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('insurance:create')")
    @Operation(summary = "Create insurance option")
    @PostMapping
    public ResponseData<?> createInsuranceOption(@RequestBody @Valid InsuranceOptionRequest request){
        log.info("Create insurance option");
        try{
            insuranceOptionService.createInsuranceOption(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create insurance option successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create insurance option failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('insurance:update')")
    @Operation(summary = "Update insurance option")
    @PutMapping("/{id}")
    public ResponseData<?> updateInsuranceOption(@PathVariable @Min(1) long id, @RequestBody @Valid InsuranceOptionRequest request){
        log.info("Update insurance option");
        try{
            insuranceOptionService.updateInsuranceOption(id, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update insurance option successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update insurance option failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('insurance:delete')")
    @Operation(summary = "Delete insurance option")
    @DeleteMapping("/{id}")
    public ResponseData<?> deleteInsuranceOption(@PathVariable @Min(1) long id){
        log.info("Delete insurance option");
        try{
            insuranceOptionService.deleteInsuranceOption(id);
            return new ResponseData<>(HttpStatus.OK.value(), "Delete insurance option successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Delete insurance option failed: " + e.getMessage());
        }
    }
}
