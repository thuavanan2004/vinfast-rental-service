package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.*;
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
import com.vinfast.rental_service.service.CarService;
import com.vinfast.rental_service.service.common.CarExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
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
    public void exportCars(HttpServletResponse response, Pageable pageable) throws IOException {
        List<Car> cars;
        if (pageable == null || pageable.isUnpaged()) {
            cars = carRepository.findAll();
        } else {
            Page<Car> carPage = carRepository.findAll(pageable);
            cars = carPage.getContent();
        }


        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=cars.xlsx");

        carExcelService.exportToExcel(cars, response.getOutputStream());
    }

    @Transactional
    @Override
    public ImportResult importCars(MultipartFile file) throws IOException {
        List<Map<String, Object>> rows = carExcelService.importFromExcel(file);

        int total = rows.size();

        Set<String> modelNames = rows.stream().map(map -> map.get("carModelName").toString()).collect(Collectors.toSet());
        Set<String> locationNames = rows.stream().map(map -> map.get("pickupLocationName").toString()).collect(Collectors.toSet());

        Map<String, CarModel> carModelMap = carModelRepository.findByNameIn(modelNames).stream().collect(Collectors.toMap(CarModel::getName, m -> m));

        Map<String, PickupLocation> pickupLocationMap = pickupLocationRepository.findByNameIn(locationNames).stream()
                .collect(Collectors.toMap(PickupLocation::getName, p -> p));

        List<RowError> errors = new ArrayList<>();
        int success = 0;

        for (int i = 0; i < rows.size(); i ++){
            Map<String, Object> map = rows.get(i);
            int rowNum = i + 2;

            Car car = (Car) map.get("car");
            String modelName = map.get("carModelName").toString();
            String locationName = map.get("pickupLocationName").toString();

            if(!carModelMap.containsKey(modelName)){
                errors.add(new RowError(i, "Car model does not exist " + modelName));
                continue;
            }

            if(!pickupLocationMap.containsKey(locationName)){
                errors.add(new RowError(i, "Pickup Location does not exist " + locationName));
                continue;
            }

            car.setCarModel(carModelMap.get(modelName));
            car.setPickupLocation(pickupLocationMap.get(locationName));

            if(carRepository.existsByVinNumber(car.getVinNumber())){
                errors.add(new RowError(rowNum, "VinNumber already exist: " + car.getVinNumber()));
                continue;
            }

            if (carRepository.existsByLicensePlate(car.getLicensePlate())) {
                errors.add(new RowError(rowNum, "License plate already exist: " + car.getLicensePlate()));
                continue;
            }

            try {
                carRepository.save(car);
                success++;
            } catch (DataIntegrityViolationException ex) {
                errors.add(new RowError(rowNum, "Internal server error: " + ex.getMostSpecificCause().getMessage()));
            }
        }

        return new ImportResult(total, success, errors);
    }

    @Override
    public ImportResult updateCarWithExcel(MultipartFile file) throws IOException {
        List<Map<String, Object>> rows = carExcelService.importFromExcel(file);
        int total = rows.size();
        int success = 0;
        List<RowError> errors = new ArrayList<>();

        Set<String> modelNames = rows.stream().map(map -> map.get("carModelName").toString()).collect(Collectors.toSet());
        Set<String> locationNames = rows.stream().map(map -> map.get("pickupLocationName").toString()).collect(Collectors.toSet());

        Map<String, CarModel> carModelMap = carModelRepository.findByNameIn(modelNames).stream().collect(Collectors.toMap(CarModel::getName, cm -> cm));
        Map<String, PickupLocation> locationMap = pickupLocationRepository.findByNameIn(locationNames).stream().collect(Collectors.toMap(PickupLocation::getName, pl -> pl));

        List<Car> toSave = new ArrayList<>();
        for (int i = 0; i < total; i ++){
            Map<String, Object> map = rows.get(i);
            int rowNumber = i + 2;

            Car car = (Car) map.get("car");
            Car carUpdate = carRepository.findByVinNumber(car.getVinNumber());
            if(carUpdate == null){
                errors.add(new RowError(rowNumber, "Car to be updated does not exist. " + car.getVinNumber()));
                continue;
            }

            String modelName = map.get("carModelName").toString();
            String locationName = map.get("pickupLocationName").toString();
            if(!carModelMap.containsKey(modelName)){
                errors.add(new RowError(rowNumber, "Car model does not exist " + modelName));
                continue;
            }

            if(!locationMap.containsKey(locationName)){
                errors.add(new RowError(rowNumber, "Pickup Location does not exist " + locationName));
                continue;
            }

            carUpdate.setCarModel(carModelMap.get(modelName));
            carUpdate.setPickupLocation(locationMap.get(locationName));

            carUpdate.setLicensePlate(car.getLicensePlate());
            carUpdate.setVinNumber(car.getVinNumber());
            carUpdate.setColor(car.getColor());
            carUpdate.setManufacturingDate(car.getManufacturingDate());
            carUpdate.setCurrentMileage(car.getCurrentMileage());
            carUpdate.setStatus(car.getStatus());
            carUpdate.setLastMaintenanceDate(car.getLastMaintenanceDate());
            carUpdate.setNextMaintenanceMileage(car.getNextMaintenanceMileage());
            carUpdate.setBatteryHealth(car.getBatteryHealth());
            carUpdate.setCreatedAt(car.getCreatedAt());
            carUpdate.setUpdatedAt(car.getUpdatedAt());

            toSave.add(carUpdate);
            success ++;
        }

        if (!toSave.isEmpty()) {
            carRepository.saveAll(toSave);
        }

        return new ImportResult(total, success, errors);
    }
}
