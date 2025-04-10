package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.PromotionApplicableModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionApplicableModelRepository extends JpaRepository<PromotionApplicableModel, Long> {
}
