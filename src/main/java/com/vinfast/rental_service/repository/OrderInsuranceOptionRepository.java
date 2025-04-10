package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.OrderInsuranceOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderInsuranceOptionRepository extends JpaRepository<OrderInsuranceOption, Long> {
}
