package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    @Query("SELECT r FROM Role r LEFT JOIN FETCH r.permissions WHERE r.id = :roleId")
    Optional<Role> findByIdWithPermissions(@Param("roleId") Long roleId);

}
