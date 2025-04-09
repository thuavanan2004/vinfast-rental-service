package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);


    @Query(value = """
            SELECT 
                DATE_FORMAT(u.created_at, :dateFormat) AS period,
                COUNT(u.id) as userCount
            FROM users u
            WHERE u.created_at BETWEEN :start AND :end
            GROUP BY period
            ORDER BY period ASC
            """, nativeQuery = true)
    List<Object[]> findPeriodStats(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("dateFormat") String dateFormat);
}
