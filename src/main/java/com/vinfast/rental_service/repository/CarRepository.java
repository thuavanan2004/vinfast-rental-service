package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.repository.projections.CarStatsProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>, JpaSpecificationExecutor<Car> {
    boolean existsByLicensePlateOrVinNumber(String licensePlate, String vinNumber);

    boolean existsByLicensePlateAndIdNot(String licensePlate, long carId);

    boolean existsByVinNumberAndIdNot(String vinNumber, long carId);

    boolean existsByLicensePlate(String licensePlate);

    boolean existsByVinNumber(String vinNumber);

    Car findByVinNumber(String vinNumber);

    Page<Car> findAllByCarModelId(long carModelId, Pageable pageable);

    @Query(value = """
        SELECT
             cm.id AS carModelId,
             cm.name AS carModelName,
             ci.image_url AS carImage,
             COUNT(ro.id) AS rentalCount,
             COALESCE(SUM(ro.total_price), 0) AS totalRevenue
        FROM car_models cm
        LEFT JOIN cars c ON cm.id = c.car_model_id
        LEFT JOIN car_images ci\s
            ON cm.id = ci.car_model_id AND ci.is_primary = 1
        LEFT JOIN rental_orders ro\s
            ON c.id = ro.car_id\s
            AND ro.status = 'completed'\s
            AND ro.created_at BETWEEN :start AND :end
        GROUP BY cm.id, cm.name, ci.image_url
        ORDER BY rentalCount DESC
        LIMIT 5; 
        """, nativeQuery = true)
    List<CarStatsProjection> findPeriodStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat
    );
}
