package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOrderDetailResponse {
    private Long id;
    private UserBasicInfo user;
    private CarInfo car;
    private PickupLocationInfo pickupLocation;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private LocalDateTime actualReturnDatetime;
    private String rentalType;
    private BigDecimal basePrice;
    private BigDecimal insuranceFee;
    private BigDecimal additionalFee;
    private BigDecimal discountAmount;
    private BigDecimal totalPrice;
    private String status;
    private String specialRequests;
    private List<InsuranceOptionInfo> insuranceOptions;
    private List<PaymentInfo> payments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Nested DTOs
    @Data
    @Builder
    public static class UserBasicInfo {
        private Long id;
        private String name;
        private String email;
        private String phone;
    }

    @Data
    @Builder
    public static class CarInfo {
        private Long id;
        private String licensePlate;
        private String vinNumber;
        private String color;
        private CarModelInfo model;
    }

    @Data
    @Builder
    public static class CarModelInfo {
        private Long id;
        private String name;
        private String modelCode;
        private String vehicleType;
        private String imageUrl;
    }

    @Data
    @Builder
    public static class PickupLocationInfo {
        private Long id;
        private String name;
        private String address;
        private String city;
    }

    @Data
    @Builder
    public static class InsuranceOptionInfo {
        private Long id;
        private String name;
        private BigDecimal dailyRate;
        private BigDecimal fee;
    }

    @Data
    @Builder
    public static class PaymentInfo {
        private Long id;
        private BigDecimal amount;
        private String paymentMethod;
        private String paymentStatus;
        private LocalDateTime paymentDate;
    }
}