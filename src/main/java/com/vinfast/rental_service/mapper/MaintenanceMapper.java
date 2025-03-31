package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.MaintenanceRequest;
import com.vinfast.rental_service.dtos.response.MaintenanceDetailResponse;
import com.vinfast.rental_service.dtos.response.MaintenanceResponse;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.MaintenanceLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MaintenanceMapper {
    MaintenanceLog toEntity(MaintenanceRequest request);

    @Mapping(target = "carInfo", source = "car", qualifiedByName = "mapCarInfo")
    MaintenanceDetailResponse toDetailDTO(MaintenanceLog entity);

    @Named("mapCarInfo")
    default MaintenanceDetailResponse.CarInfo mapCarInfo(Car car){
        return new MaintenanceDetailResponse.CarInfo(
                car.getId(),
                car.getLicensePlate(),
                car.getVinNumber(),
                car.getColor(),
                car.getManufacturingDate(),
                car.getCurrentMileage(),
                car.getStatus(),
                car.getLastMaintenanceDate(),
                car.getNextMaintenanceMileage(),
                car.getBatteryHealth()
        );
    }

    @Mapping(target = "licensePlate", source = "car.licensePlate")
    MaintenanceResponse toDTO(MaintenanceLog entity);
}
