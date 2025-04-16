package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface PickupLocationRepository extends JpaRepository<PickupLocation, Long> {
    PickupLocation findByName(String name);

    List<PickupLocation> findByNameIn(Set<String> names);
}
