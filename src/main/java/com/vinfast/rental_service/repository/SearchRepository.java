package com.vinfast.rental_service.repository;

import com.vinfast.rental_service.dtos.response.PageResponse;
import com.vinfast.rental_service.dtos.response.RentalOrderResponse;
import com.vinfast.rental_service.mapper.RentalOrderMapper;
import com.vinfast.rental_service.model.Car;
import com.vinfast.rental_service.model.RentalOrder;
import com.vinfast.rental_service.repository.specification.SpecSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.vinfast.rental_service.utils.AppConst.SEARCH_SPEC_OPERATOR;

@Repository
@Slf4j
@RequiredArgsConstructor
public class SearchRepository {
    @PersistenceContext
    private final EntityManager entityManager;

    private final RentalOrderMapper rentalOrderMapper;

    public PageResponse<?> searchRentalOrderWithJoin(Pageable pageable, String[] orders, String[] cars){
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<RentalOrder> query = builder.createQuery(RentalOrder.class);
        Root<RentalOrder> orderRoot = query.from(RentalOrder.class);
        Join<RentalOrder, Car> carRoot = orderRoot.join("car");

        List<Predicate> orderPreList = new ArrayList<>();
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        for(String o : orders){
            Matcher matcher = pattern.matcher(o);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                orderPreList.add(toOrderPredicate(orderRoot, builder, criteria));
            }
        }

        List<Predicate> carPreList = new ArrayList<>();
        for(String c : cars){
            Matcher matcher = pattern.matcher(c);
            if(matcher.find()){
                SpecSearchCriteria criteria = new SpecSearchCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                carPreList.add(toCarPredicate(carRoot, builder, criteria));
            }
        }

        Predicate orderPre = builder.or(orderPreList.toArray(new Predicate[0]));
        Predicate carPre = builder.or(carPreList.toArray(new Predicate[0]));
        Predicate finalPre = builder.and(orderPre, carPre);

        query.where(finalPre);

        List<RentalOrder> records = entityManager.createQuery(query).setFirstResult(pageable.getPageNumber()).setMaxResults(pageable.getPageSize()).getResultList();
        List<RentalOrderResponse> results = records.stream().map(rentalOrderMapper::toDTO).toList();

        return PageResponse.builder()
                .page(pageable.getPageNumber())
                .size(pageable.getPageSize())
                .items(results)
                .build();
    }

    public Predicate toOrderPredicate(Root<RentalOrder> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()){
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    public Predicate toCarPredicate(Join<RentalOrder, Car> root, CriteriaBuilder criteriaBuilder, SpecSearchCriteria criteria) {
        return switch (criteria.getOperation()){
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

}
