package com.vinfast.rental_service.modules.admin.domain.repository;

import com.vinfast.rental_service.modules.admin.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}
