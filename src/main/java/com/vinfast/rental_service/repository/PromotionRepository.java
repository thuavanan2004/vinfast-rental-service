package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.enums.PromotionStatus;
import com.vinfast.rental_service.model.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Page<Promotion> findByStatus(PromotionStatus status, Pageable pageable);

    Optional<Promotion> findByCode(String code);
}
