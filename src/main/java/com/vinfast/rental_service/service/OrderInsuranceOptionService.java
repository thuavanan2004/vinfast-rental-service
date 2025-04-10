package com.vinfast.rental_service.service;

import com.vinfast.rental_service.model.InsuranceOption;
import com.vinfast.rental_service.model.OrderInsuranceOption;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.repository.OrderInsuranceOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class OrderInsuranceOptionService {
    private final OrderInsuranceOptionRepository orderInsuranceOptionRepository;

    public void create(RentalOrder rentalOrder, InsuranceOption insuranceOption, BigDecimal fee){
        OrderInsuranceOption orderInsuranceOption = OrderInsuranceOption.builder()
                .order(rentalOrder)
                .insuranceOption(insuranceOption)
                .fee(insuranceOption.getDailyRate())
                .build();

        orderInsuranceOptionRepository.save(orderInsuranceOption);
    }
}
