package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.RentalOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long>, JpaSpecificationExecutor<RentalOrder> {
    Page<RentalOrder> findAllByUserId(long userId, Pageable pageable);
}
