package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.enums.PaymentMethod;
import com.vinfast.rental_service.model.Payment;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment createPayment(RentalOrder order, PaymentMethod method) {
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalPrice())
                .paymentMethod(method)
                .build();
        return paymentRepository.save(payment);
    }
}