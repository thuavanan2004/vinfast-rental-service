package com.vinfast.rental_service.controllers.admin;

import com.vinfast.rental_service.dtos.request.LocationRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.PickupLocationStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.service.CarModelService;
import com.vinfast.rental_service.service.LocationService;
import com.vinfast.rental_service.validate.EnumPattern;
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

@Tag(name = "Location management")
@Slf4j
@Validated
@RestController
@RequestMapping("/api/admin/locations")
@RequiredArgsConstructor
public class AdminLocationController {

    private final LocationService locationService;

    @PreAuthorize("hasAuthority('pickup_location:read')")
    @Operation(summary = "Get location by id")
    @GetMapping("/{locationId}")
    public ResponseData<?> getLocationById(@PathVariable @Min (1) long locationId){
        log.info("Get location by id");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get location by id successfully", locationService.getLocationById(locationId));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get location by id failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('pickup_location:create')")
    @Operation(summary = "Create new pickup location")
    @PostMapping
    public ResponseData<?> createLocation(@RequestBody @Valid LocationRequest request){
        log.info("Create new pickup location");
        try{
            locationService.createLocation(request);
            return new ResponseData<>(HttpStatus.OK.value(), "Create new pickup location successfully");
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Create new pickup location failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('pickup_location:update')")
    @Operation(summary = "Update pickup location")
    @PutMapping("/{locationId}")
    public ResponseData<?> updateLocation(@PathVariable @Min (1) long locationId, @RequestBody @Valid LocationRequest request){
        log.info("Update pickup location");
        try{
            locationService.updateLocation(locationId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update pickup location successfully");
        } catch(ResourceNotFoundException e) {
            log.error("Update pickup location: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Update pickup location failed: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('pickup_location:update')")
    @Operation(summary = "Change status location")
    @PatchMapping("/status/{locationId}")
    public ResponseData<?> changeStatus(@PathVariable @Min (1) long locationId,
                                        @EnumPattern(name = "status", regexp = "active|inactive") @RequestParam PickupLocationStatus status){
        log.info("Change status location");
        try{
            locationService.changeStatus(locationId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Change status location successfully");
        } catch(ResourceNotFoundException e) {
            log.error("Change status location: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch(InvalidDataException e) {
            log.error("Change status location: InvalidDataException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Change status location failed: " + e.getMessage());
        }
    }
}
