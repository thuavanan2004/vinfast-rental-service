package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
