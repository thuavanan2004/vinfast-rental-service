package com.vinfast.rental_service.controllers.admin;


import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.ResponseData;
import com.vinfast.rental_service.dtos.response.ResponseError;
import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.service.CarService;
import com.vinfast.rental_service.validate.EnumPattern;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Tag(name = "Car management")
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/cars")
public class AdminCarController {

    private final CarService carService;

    @PreAuthorize("hasAuthority('car:read')")
    @Operation(summary = "Get list car")
    @GetMapping("")
    public ResponseData<?> getCars(Pageable pageable, @RequestParam(required = false) String[] cars){
        log.info("Get list car");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list car successfully", carService.getCars(pageable, cars));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list car failed");
        }
    }

    @PreAuthorize("hasAuthority('car:read')")
    @Operation(summary = "Get list car by car model id")
    @GetMapping("/car-models/{carModelId}")
    public ResponseData<?> getListCarByCarModel(@PathVariable @Min(1) long carModelId, Pageable pageable){
        log.info("Get list car by car model id");
        try{
            return new ResponseData<>(HttpStatus.OK.value(), "Get list car by car model id successfully", carService.getListCarByCarModel(carModelId, pageable));
        }catch (Exception e){
            log.error("errorMessage={}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.BAD_REQUEST.value(), "Get list car by car model id failed");
        }
    }

    @PreAuthorize("hasAuthority('car:read')")
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

    @PreAuthorize("hasAuthority('car:create')")
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

    @PreAuthorize("hasAuthority('car:update')")
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

    @PreAuthorize("hasAuthority('car:update')")
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

    @PreAuthorize("hasAuthority('maintenance:create')")
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

    @PreAuthorize("hasAuthority('maintenance:read')")
    @Operation(summary = "Get maintenance-report by maintenanceId")
    @GetMapping("/{maintenanceId}/maintenance-report")
    public ResponseData<?> maintenanceReport(@PathVariable @Min(1) long maintenanceId){
        log.info("Get maintenance-report by maintenanceId");
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

    @PreAuthorize("hasAuthority('maintenance:read')")
    @Operation(summary = "Get list maintenance-report by carId")
    @GetMapping("/maintenance-report/list/{carId}")
    public ResponseData<?> maintenanceReportsByCarId(@PathVariable @Min(1) long carId, Pageable pageable){
        log.info("Get list maintenance-report by carId");
        try{

            return new ResponseData<>(HttpStatus.CREATED.value(), "Get list maintenance-report successfully", carService.maintenanceReportsByCarId(carId, pageable));
        }catch (ResourceNotFoundException e) {
            log.error("Get list maintenance: ResourceNotFoundException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('car:read')")
    @Operation(summary = "Get file excel")
    @GetMapping("/export/excel")
    public ResponseData<?> exportCarToExcel(HttpServletResponse response, Pageable pageable, HttpServletRequest request) {
        log.info("Export file info car to excel ");
        try{
            carService.exportCars(response, pageable, request);
            return new ResponseData<>(HttpStatus.CREATED.value(),
                    "Export file info car to excel  successfully");
        }catch (IOException e) {
            log.error("Export file: IOException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('car:read')")
    @Operation(summary = "Get file excel")
    @PostMapping("/import/excel")
    public ResponseData<?> importCarToExcel(@RequestParam("file") MultipartFile file) {
        log.info("Import file info car to excel ");
        try{
            return new ResponseData<>(HttpStatus.CREATED.value(),
                    "Import file info car to excel successfully",
                    carService.importCars(file));
        }catch (IOException e) {
            log.error("Import file: IOException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }

    @PreAuthorize("hasAuthority('car:read')")
    @Operation(summary = "Update list car with excel")
    @PutMapping("/update/excel")
    public ResponseData<?> updateCarWithExcel(@RequestParam("file") MultipartFile file) {
        log.info("Update list car with excel");
        try{
            return new ResponseData<>(HttpStatus.CREATED.value(),
                    "Update list car with excel successfully",
                    carService.updateCarWithExcel(file));
        }catch (IOException e) {
            log.error("Update car with file: IOException: {}", e.getMessage());
            return new ResponseError(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error: {}", e.getMessage(), e.getCause());
            return new ResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error");
        }
    }
}
