package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface LocationRepository extends JpaRepository<PickupLocation, Long> {
    List<PickupLocation> findByNameIn(Set<String> names);
}
