package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByLicensePlateOrVinNumber(String licensePlate, String vinNumber);

    boolean existsByLicensePlateAndIdNot(String licensePlate, long carId);

    boolean existsByVinNumberAndIdNot(String vinNumber, long carId);
}
