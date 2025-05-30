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
import com.vinfast.rental_service.model.*;
import com.vinfast.rental_service.repository.CarModelRepository;
import com.vinfast.rental_service.repository.CarRepository;
import com.vinfast.rental_service.repository.MaintenanceRepository;
import com.vinfast.rental_service.repository.PickupLocationRepository;
import com.vinfast.rental_service.repository.specification.CarSpecificationBuilder;
import com.vinfast.rental_service.repository.specification.UserSpecificationBuilder;
import com.vinfast.rental_service.service.CarService;
import com.vinfast.rental_service.service.common.CarExcelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.vinfast.rental_service.utils.AppConst.SEARCH_SPEC_OPERATOR;

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
    public PageResponse<?> getCars(Pageable pageable, String[] cars) {

        Page<Car> records;
        if(cars != null){
            CarSpecificationBuilder builder = new CarSpecificationBuilder();
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String c : cars){
                Matcher matcher = pattern.matcher(c);
                if(matcher.find()){
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                }
            }
            records = carRepository.findAll(Objects.requireNonNull(builder.build()), pageable);
        }else {
            records = carRepository.findAll(pageable);
        }

        List<CarResponse> list = records.stream().map(carMapper::toDTO).toList();

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
    public void exportCars() throws IOException {
        List<Car> cars = carRepository.findAll();

        String filePath = "C:\\Users\\Admin\\Downloads\\cars.xlsx";
        carExcelService.exportToExcel(cars, filePath);
    }

    @Transactional
    @Override
    public void importCars(MultipartFile file) throws IOException {
        List<Map<String, Object>> cars = carExcelService.importFromExcel(file);

        Set<String> modelNames = cars.stream().map(map -> map.get("carModelName").toString()).collect(Collectors.toSet());
        Set<String> locationNames = cars.stream().map(map -> map.get("pickupLocationName").toString()).collect(Collectors.toSet());

        Map<String, CarModel> carModelMap = carModelRepository.findByNameIn(modelNames).stream().collect(Collectors.toMap(CarModel::getName, m -> m));

        Map<String, PickupLocation> pickupLocationMap = pickupLocationRepository.findByNameIn(locationNames).stream()
                .collect(Collectors.toMap(PickupLocation::getName, p -> p));

        List<Car> records = cars.stream().map(map -> {
            String modelName = map.get("carModelName").toString();
            String locationName = map.get("pickupLocationName").toString();

            CarModel carModel = carModelMap.get(modelName);
            PickupLocation pickupLocation = pickupLocationMap.get(locationName);

            Car car = (Car) map.get("car");
            car.setCarModel(carModel);
            car.setPickupLocation(pickupLocation);
            return car;
        }).collect(Collectors.toList());

        carRepository.saveAll(records);
        log.info("Import file successfully");
    }
}
