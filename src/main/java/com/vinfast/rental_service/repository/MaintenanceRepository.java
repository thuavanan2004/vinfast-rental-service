package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.dtos.response.MaintenanceResponse;
import com.vinfast.rental_service.model.MaintenanceLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<MaintenanceLog, Long> {
    Page<MaintenanceLog> findAllByCarId(long carId, Pageable pageable);
}
