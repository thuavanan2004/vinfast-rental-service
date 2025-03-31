package com.vinfast.rental_service.service.Impl;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderResponse;
import com.vinfast.rental_service.mapper.RentalOrderMapper;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.repository.RentalOrderRepository;
import com.vinfast.rental_service.repository.SearchRepository;
import com.vinfast.rental_service.repository.specification.RentalOrderSpecificationBuilder;
import com.vinfast.rental_service.service.RentalOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vinfast.rental_service.utils.AppConst.SEARCH_SPEC_OPERATOR;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalOrderServiceImpl implements RentalOrderService {
    private final RentalOrderRepository rentalOrderRepository;

    private final RentalOrderMapper rentalOrderMapper;

    private final SearchRepository searchRepository;

    @Override
    public PageResponse<?> getAll(Pageable pageable, String[] orders, String[] cars) {
        Page<RentalOrder> records;
        if(orders != null && cars != null){
            return searchRepository.searchRentalOrderWithJoin(pageable, orders, cars);
        } else if(orders != null){
            RentalOrderSpecificationBuilder builder = new RentalOrderSpecificationBuilder();
            Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
            for (String o : orders){
                Matcher matcher = pattern.matcher(o);
                if(matcher.find()){
                    builder.with(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                }
            }
            records = rentalOrderRepository.findAll(Objects.requireNonNull(builder.build()), pageable);
        } else {
            records = rentalOrderRepository.findAll(pageable);
        }

        List<RentalOrderResponse> results = records.stream().map(rentalOrderMapper::toDTO).toList();
        return PageResponse.builder()
                .page(records.getNumber())
                .size(records.getTotalPages())
                .totalPage(records.getTotalPages())
                .items(results)
                .build();
    }
}
