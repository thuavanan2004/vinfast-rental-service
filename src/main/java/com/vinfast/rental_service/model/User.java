package com.vinfast.rental_service.model;

import com.vinfast.rental_service.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@Table(name = "users")
@Entity
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(length = 50)
    private String drivingLicenseNumber;

    @Column(length = 255)
    private String drivingLicenseImage;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private Boolean verified = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.active;

    @Column(nullable = false, updatable = false)
    private LocalDate createdAt;

    @Column(nullable = false)
    private LocalDate updatedAt;

}
