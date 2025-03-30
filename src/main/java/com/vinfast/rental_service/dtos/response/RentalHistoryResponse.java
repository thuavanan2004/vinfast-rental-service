package com.vinfast.rental_service.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.vinfast.rental_service.enums.RentalOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a rental history response with detailed information
 *
 * Immutable and thread-safe by design using Java records
 */
public record RentalHistoryResponse(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Long orderId,

        CarDetails carDetails,
        RentalPeriod rentalPeriod,

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        BigDecimal totalAmount,

        RentalOrderStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        PickupLocation pickupLocation
) {

    public record CarDetails(
            String modelName,
            String licensePlate,
            String color,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            String imageUrl
    ) {}

    public record RentalPeriod(
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime startDate,

            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime endDate,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime actualReturnDate
    ) {}

    public record PickupLocation(
            String name,
            String address,
            String contactPhone
    ) {}
}