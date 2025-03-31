package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.PickupLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<PickupLocation, Long> {
}
