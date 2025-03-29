package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.RentalOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    Page<RentalOrder> findAllByUserId(long userId, Pageable pageable);
}
