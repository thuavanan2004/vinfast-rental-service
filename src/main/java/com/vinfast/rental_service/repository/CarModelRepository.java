package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.enums.RentalType;
import com.vinfast.rental_service.model.CarModel;
import com.vinfast.rental_service.repository.projections.CarModelProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CarModelRepository extends JpaRepository<CarModel, Long> {
    boolean existsByModelCode(String modelCode);

    @Query(value = """
        SELECT DISTINCT cm.id as id, cm.name as name, cm.model_code as modelCode, ci.image_url as mainImage,
               cm.vehicle_type as vehicleType, cm.range_per_charge as rangePerCharge, 
               cm.trunk_capacity as trunkCapacity, cm.seating_capacity as seatingCapacity, 
               CASE 
                   WHEN :rentalType = 'daily' THEN cm.base_price_per_day
                   WHEN :rentalType = 'weekly' THEN cm.base_price_per_week
                   WHEN :rentalType = 'monthly' THEN cm.base_price_per_month
                   ELSE cm.base_price_per_year
               END as basePrice, 
               CASE 
                    WHEN EXISTS (
                        SELECT 1 FROM cars c
                        JOIN pickup_locations pl ON c.pickup_location_id = pl.id
                        WHERE c.car_model_id = cm.id
                        AND pl.city = :city
                        AND pl.status = 'active'
                        AND c.status = 'available'
                        AND NOT EXISTS (
                            SELECT 1 FROM rental_orders ro
                            WHERE ro.car_id = c.id
                            AND ro.status NOT IN ('CANCELLED', 'COMPLETED')
                            AND (
                                ro.start_datetime < :returnTime
                                AND ro.end_datetime > :pickupTime
                            )
                        )
                    )  THEN 1
                    ELSE 0
               END as available          
        FROM car_models cm 
        JOIN car_images ci 
            ON ci.car_model_id = cm.id
            AND ci.is_primary = 1
    """, nativeQuery = true)
    List<CarModelProjection> findAvailableModels(
            @Param("city") String city,
            @Param("pickupTime") LocalDateTime pickupTime,
            @Param("returnTime") LocalDateTime returnTime,
            @Param("rentalType") RentalType rentalType
    );

    CarModel findByName(String name);
}
