package com.vinfast.rental_service.mapper;

import com.vinfast.rental_service.dtos.response.RentalOrderResponse;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.Payment;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RentalOrderMapper {
    @Mapping(target = "customer", source = "user", qualifiedByName = "mapByUser")
    @Mapping(target = "car", source = "car", qualifiedByName = "mapByCar")
    @Mapping(target = "paymentStatus", source = "payments", qualifiedByName = "mapByPayments")
    RentalOrderResponse toDTO(RentalOrder entity);

    @Named("mapByUser")
    default RentalOrderResponse.CustomerBasicInfo mapByUser(User user){
        return new RentalOrderResponse.CustomerBasicInfo(
                user.getId(),
                user.getName(),
                user.getPhone()
        );
    }

    @Named("mapByCar")
    default RentalOrderResponse.CarBasicInfo mapByCar(Car car){
        return new RentalOrderResponse.CarBasicInfo(
                car.getId(),
                car.getLicensePlate(),
                car.getCarModel().getName(),
                car.getCarModel().getImages().get(0).getImageUrl()
        );
    }

    @Named("mapByPayments")
    default  String mapByPayments(List<Payment> payments){
        return payments.get(0).getPaymentStatus().toString();
    }

}
