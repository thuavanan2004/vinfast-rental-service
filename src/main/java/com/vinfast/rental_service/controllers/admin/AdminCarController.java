package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarModelCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.service.CarModelService;
import com.vinfast.rental_service.service.CarService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Car management")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cars")
public class AdminCarController {

    private final CarService carService;

    @Operation(summary = "Get car to the system")
    @GetMapping("/{carId}")
    public ResponseData<?> getInfoCar(@PathVariable @Min(1) long carId){
        log.info("Get car to the system");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get car to the system successfully", carService.getInfoCar(carId));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get car to the system failed");
        }
    }


    @Operation(summary = "Add new car to the system")
    @PostMapping("/{carModelId}")
    public ResponseData<?> addNewCar(@PathVariable @Min(1) long carModelId, @Valid @RequestBody CarCreateRequest request){
        log.info("Add new car to the system");
        try{
            carService.addNewCar(carModelId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Add new car to the system successfully");
        }catch (ResourceNotFoundException e) {
            log.error("Add new car to the system: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (InvalidDataException e) {
            log.error("Add new car to the system: InvalidDataException: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @Operation(summary = "Update car status")
    @PutMapping("/status/{carId}")
    public ResponseData<?> updateCarStatus(@PathVariable @Min(1) long carId,
                                           @RequestParam @EnumPattern(name = "status", regexp = "available|rented|maintenance|unavailable") CarStatus status){
        log.info("Update car status");
        try{
            carService.updateCarStatus(carId, status);
            return new ResponseData<>(HttpStatus.OK.value(), "Update car status successfully");
        }catch (ResourceNotFoundException e) {
            log.error("Update car status: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (InvalidDataException e) {
            log.error("Update car status: InvalidDataException: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @Operation(summary = "Update info car")
    @PutMapping("/{carId}")
    public ResponseData<?> updateCar(@PathVariable @Min(1) long carId,
                                     @RequestBody @Valid CarUpdateRequest request){
        log.info("Update car");
        try{
            carService.updateCar(carId, request);
            return new ResponseData<>(HttpStatus.OK.value(), "Update info car successfully");
        }catch (ResourceNotFoundException e) {
            log.error("Update info car: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (InvalidDataException e) {
            log.error("Update info car: InvalidDataException: {}", e.getMessage());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @Operation(summary = "Create maintenance for car")
    @PostMapping("/{carId}/maintenance")
    public ResponseData<?> createMaintenance(@PathVariable @Min(1) long carId,
                                             @RequestBody @Valid MaintenanceRequest request){
        log.info("Create maintenance for car");
        try{
            carService.createMaintenance(carId, request);
            return new ResponseData<>(HttpStatus.CREATED.value(), "Create maintenance successfully");
        }catch (ResourceNotFoundException e) {
            log.error("Create maintenance: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @Operation(summary = "Get maintenance-report for car")
    @GetMapping("/{maintenanceId}/maintenance-report")
    public ResponseData<?> maintenanceReport(@PathVariable @Min(1) long maintenanceId){
        log.info("Get maintenance for car");
        try{

            return new ResponseData<>(HttpStatus.CREATED.value(), "Get maintenance-report successfully", carService.maintenanceReport(maintenanceId));
        }catch (ResourceNotFoundException e) {
            log.error("Get maintenance: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }
}
