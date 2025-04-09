package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {
    Optional<OtpCode> findByEmailAndCode(String email, String code);
}
