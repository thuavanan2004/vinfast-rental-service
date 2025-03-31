package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.MaintenanceLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaintenanceRepository extends JpaRepository<MaintenanceLog, Long> {
}
