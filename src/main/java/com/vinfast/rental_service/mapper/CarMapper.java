package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.request.CarCreateRequest;
import com.vinfast.rental_service.dtos.request.CarUpdateRequest;
import com.vinfast.rental_service.dtos.response.CarResponse;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.model.PickupLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface CarMapper {
    @Mapping(target = "pickupLocation", ignore = true)
    Car toEntity(CarCreateRequest request);

    @Mapping(target = "carModel", source = "carModel", qualifiedByName = "mapCarModel")
    @Mapping(target = "pickupLocation", source = "pickupLocation", qualifiedByName = "mapPickupLocation")
    CarResponse toDTO(Car car);

    @Named("mapCarModel")
    default CarResponse.CarModelDto mapCarModel(CarModel carModel){
        return new CarResponse.CarModelDto(
                carModel.getId(),
                carModel.getName(),
                carModel.getModelCode(),
                carModel.getVehicleType(),
                carModel.getBatteryCapacity(),
                carModel.getRangePerCharge(),
                carModel.getSeatingCapacity(),
                carModel.getBasePricePerDay()
        );
    }

    @Named("mapPickupLocation")
    default CarResponse.PickupLocationDto mapPickupLocation(PickupLocation pickupLocation){
        if (pickupLocation == null) return null;
        return new CarResponse.PickupLocationDto(
                pickupLocation.getId(),
                pickupLocation.getName(),
                pickupLocation.getAddress(),
                pickupLocation.getCity()
        );
    }

    void updateCar(CarUpdateRequest request, @MappingTarget Car car);
}
