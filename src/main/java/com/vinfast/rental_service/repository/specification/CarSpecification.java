package com.vinfast.rental_service.repository.specification;

import com.vinfast.rental_service.model.Car;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Getter
@RequiredArgsConstructor
public class CarSpecification implements Specification<Car> {

    private final SpecSearchCriteria criteria;

    @Override
    public Predicate toPredicate(@NonNull Root<Car> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        return switch (criteria.getOperation()){
            case EQUALITY -> criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> criteriaBuilder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> criteriaBuilder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> criteriaBuilder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString().toLowerCase() + "%");
            case STARTS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }
}
