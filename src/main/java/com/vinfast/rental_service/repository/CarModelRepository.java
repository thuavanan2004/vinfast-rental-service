package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    boolean existsByModelCode(String modelCode);
}
