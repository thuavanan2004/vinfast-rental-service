package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.CarResponse;
import com.vinfast.rental_service.dtos.response.MaintenanceDetailResponse;
import com.vinfast.rental_service.dtos.response.MaintenanceResponse;
import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.enums.CarStatus;
import com.vinfast.rental_service.exceptions.InvalidDataException;
import com.vinfast.rental_service.exceptions.ResourceNotFoundException;
import com.vinfast.rental_service.mapper.CarMapper;
import com.vinfast.rental_service.mapper.MaintenanceMapper;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.model.MaintenanceLog;
import com.vinfast.rental_service.model.PickupLocation;
import com.vinfast.rental_service.repository.CarModelRepository;
import com.vinfast.rental_service.repository.CarRepository;
import com.vinfast.rental_service.repository.MaintenanceRepository;
import com.vinfast.rental_service.repository.PickupLocationRepository;
import com.vinfast.rental_service.service.CarService;
import com.vinfast.rental_service.service.common.CarExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarModelRepository carModelRepository;

    private final CarRepository carRepository;

    private final PickupLocationRepository pickupLocationRepository;

    private final CarMapper carMapper;

    private final MaintenanceMapper maintenanceMapper;

    private final MaintenanceRepository maintenanceRepository;

    private final CarExcelService carExcelService;


    @Override
    public void addNewCar(long carModelId, CarCreateRequest request) {
        CarModel carModel = carModelRepository.findById(carModelId).orElseThrow(() -> new ResourceNotFoundException("Car model not found"));
        if (carRepository.existsByLicensePlateOrVinNumber(request.getLicensePlate(), request.getVinNumber())) {
            throw new InvalidDataException("License plate or VIN number already exists");
        }
        Car car = carMapper.toEntity(request);
        car.setCarModel(carModel);
        Optional.ofNullable(request.getPickupLocationId())
                .ifPresent(locationId -> {
                    PickupLocation pickupLocation = pickupLocationRepository.findById(locationId)
                            .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found with id: " + locationId));
                    car.setPickupLocation(pickupLocation);
                });
        carRepository.save(car);

        log.info("Add new car to the system successfully");
    }

    @Override
    public CarResponse getInfoCar(long carId) {
        Car car = carRepository.findById(carId).orElseThrow(() -> new ResourceNotFoundException("Car not found"));
        return carMapper.toDTO(car);
    }

    @Override
    public void updateCarStatus(long carId, CarStatus status) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (car.getStatus() == status) {
            throw new InvalidDataException("Car already has status: " + status);
        }
        car.setStatus(status);
        carRepository.save(car);

        log.info("Updated car ID {} status from {} to {}",
                carId, car.getStatus(), status);
    }

    @Override
    public void updateCar(long carId, CarUpdateRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        if (carRepository.existsByLicensePlateAndIdNot(request.getLicensePlate(), carId)) {
            throw new InvalidDataException("License plate " + request.getLicensePlate() + " already exists on another car");
        }

        if (carRepository.existsByVinNumberAndIdNot(request.getVinNumber(), carId)) {
            throw new InvalidDataException("VIN number " + request.getVinNumber() + " already exists on another car");
        }
        CarModel carModel = carModelRepository.findById(request.getCarModelId())
                .orElseThrow(() -> new ResourceNotFoundException("Car model not found with id: " + request.getCarModelId()));

        carMapper.updateCar(request, car);
        car.setCarModel(carModel);

        Optional.ofNullable(request.getPickupLocationId()).ifPresent(locationId -> {
            PickupLocation pickupLocation = pickupLocationRepository.findById(locationId)
                    .orElseThrow(() -> new ResourceNotFoundException("Pickup location not found with id: " + locationId));
            car.setPickupLocation(pickupLocation);
        });

        carRepository.save(car);

        log.info("Updated car ID {} with license plate {}", carId, request.getLicensePlate());
    }

    @Override
    public void createMaintenance(long carId, MaintenanceRequest request) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new ResourceNotFoundException("Car not found with id: " + carId));

        car.setStatus(CarStatus.maintenance);

        MaintenanceLog maintenanceLog = maintenanceMapper.toEntity(request);
        maintenanceLog.setCar(car);

        maintenanceRepository.save(maintenanceLog);

        log.info("Create maintenance log successfully!");
    }

    @Override
    public MaintenanceDetailResponse maintenanceReport(long maintenanceId) {
        MaintenanceLog maintenanceLog = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new ResourceNotFoundException("Maintenance not found with id: " + maintenanceId));

        return maintenanceMapper.toDetailDTO(maintenanceLog);
    }

    @Override
    public PageResponse<?> maintenanceReportsByCarId(long carId, Pageable pageable) {
        Page<MaintenanceLog> records = maintenanceRepository.findAllByCarId(carId, pageable);

        List<MaintenanceResponse> list = records.stream().map(maintenanceMapper::toDTO).toList();
        return PageResponse.builder()
                .page(records.getNumber())
                .size(records.getSize())
                .totalPage(records.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public PageResponse<?> getListCarByCarModel(long carModelId, Pageable pageable) {
        Page<Car> records = carRepository.findAllByCarModelId(carModelId, pageable);

        List<CarResponse> list = records.stream().map(carMapper::toDTO).toList();

        return PageResponse.builder()
                .page(records.getNumber())
                .size(records.getSize())
                .totalPage(records.getTotalPages())
                .items(list)
                .build();
    }

    @Override
    public void exportCars(HttpServletResponse response) throws IOException {
        List<Car> cars = carRepository.findAll();

        String filePath = "C:\\Users\\Admin\\Downloads\\cars.xlsx";
        carExcelService.exportToExcel(cars, filePath);
    }
}
