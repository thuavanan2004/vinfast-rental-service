package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.response.RentalHistoryResponse;
import com.vinfast.rental_service.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalHistoryMapper {

    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "carDetails", source = "car", qualifiedByName = "mapCarDetails")
    @Mapping(target = "rentalPeriod", source = ".", qualifiedByName = "mapRentalPeriod")
    @Mapping(target = "pickupLocation", source = "pickupLocation", qualifiedByName = "mapPickupLocation")
    RentalHistoryResponse toResponse(RentalOrder rentalOrder);

    @Named("mapCarDetails")
    default RentalHistoryResponse.CarDetails mapCarDetails(Car car) {
        if (car == null) {
            return null;
        }

        String primaryImageUrl = null;
        if (car.getCarModel() != null && car.getCarModel().getImages() != null && !car.getCarModel().getImages().isEmpty()) {
            primaryImageUrl = car.getCarModel().getImages().stream()
                    .filter(CarImage::getIsPrimary)
                    .findFirst()
                    .map(CarImage::getImageUrl)
                    .orElse(null);
        }

        return new RentalHistoryResponse.CarDetails(
                car.getCarModel() != null ? car.getCarModel().getName() : null,
                car.getLicensePlate(),
                car.getColor(),
                primaryImageUrl
        );
    }

    @Named("mapRentalPeriod")
    default RentalHistoryResponse.RentalPeriod mapRentalPeriod(RentalOrder order) {
        return new RentalHistoryResponse.RentalPeriod(
                order.getStartDatetime(),
                order.getEndDatetime(),
                order.getActualReturnDatetime()
        );
    }

    @Named("mapPickupLocation")
    default RentalHistoryResponse.PickupLocation mapPickupLocation(PickupLocation location) {
        if (location == null) {
            return null;
        }
        return new RentalHistoryResponse.PickupLocation(
                location.getName(),
                location.getAddress(),
                location.getContactPhone()
        );
    }
}