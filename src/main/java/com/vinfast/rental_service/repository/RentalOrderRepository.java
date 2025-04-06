package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.enums.RentalOrderStatus;
import com.vinfast.rental_service.model.RentalOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long>, JpaSpecificationExecutor<RentalOrder> {
    Page<RentalOrder> findAllByUserId(long userId, Pageable pageable);

    @Query("SELECT SUM(o.totalPrice) FROM RentalOrder o WHERE o.status = 'completed'")
    Double calculateTotalRevenue();

    long countByStatus(RentalOrderStatus status);

    @Query(value = """
    SELECT 
        DATE_FORMAT(o.created_at, :dateFormat) AS period,
        COALESCE(SUM(o.total_price), 0) AS totalSales,
        COUNT(o.id) AS orderCount
    FROM rental_orders o    
    WHERE o.status = 'completed'
      AND o.created_at BETWEEN :start AND :end
    GROUP BY period
    ORDER BY period ASC 
    """, nativeQuery = true)
    List<Object[]> findPeriodStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );

}
