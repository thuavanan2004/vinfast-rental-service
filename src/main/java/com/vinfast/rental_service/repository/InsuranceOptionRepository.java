package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.enums.InsuranceOptionStatus;
import com.vinfast.rental_service.model.InsuranceOption;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceOptionRepository extends JpaRepository<InsuranceOption, Long> {
    Page<InsuranceOption> findByStatus(InsuranceOptionStatus status, Pageable pageable);
}
