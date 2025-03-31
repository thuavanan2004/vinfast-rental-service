package com.vinfast.rental_service.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOrderResponse {
    private Long id;
    private String orderCode;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private BigDecimal totalPrice;
    private String status;
    private String paymentStatus;

    private CarBasicInfo car;

    private CustomerBasicInfo customer;

    private PickupLocationBasicInfo pickupLocation;

    @Data
    @AllArgsConstructor
    public static class CarBasicInfo {
        private Long id;
        private String licensePlate;
        private String modelName;
        private String imageUrl;
    }

    @Data
    @AllArgsConstructor
    public static class CustomerBasicInfo {
        private Long id;
        private String name;
        private String phone;
    }

    @Data
    @Builder
    public static class PickupLocationBasicInfo {
        private Long id;
        private String name;
        private String city;
    }
}
