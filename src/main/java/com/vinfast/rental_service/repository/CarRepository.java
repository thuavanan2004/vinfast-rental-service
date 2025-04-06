package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.repository.projections.CarStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByLicensePlateOrVinNumber(String licensePlate, String vinNumber);

    boolean existsByLicensePlateAndIdNot(String licensePlate, long carId);

    boolean existsByVinNumberAndIdNot(String vinNumber, long carId);

    Page<Car> findAllByCarModelId(long carModelId, Pageable pageable);

    @Query(value = """
        SELECT
            c.id AS carId,
            c.license_plate AS licensePlate,
            ci.image_url,
            cm.name AS carModelName,
            COUNT(ro.id) AS rentalCount,
            COALESCE(SUM(ro.total_price), 0) AS totalRevenue
        FROM cars c
        JOIN car_models cm ON c.car_model_id = cm.id
        LEFT JOIN car_images ci 
            ON cm.id = ci.car_model_id AND ci.is_primary = TRUE
        LEFT JOIN rental_orders ro 
            ON c.id = ro.car_id 
            AND ro.status = 'completed' 
            AND ro.created_at BETWEEN :start AND :end
        GROUP BY c.id, c.license_plate, ci.image_url, cm.name
        ORDER BY rentalCount DESC
        LIMIT 10;
        """, nativeQuery = true)
    List<CarStatsProjection> findPeriodStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );
}
