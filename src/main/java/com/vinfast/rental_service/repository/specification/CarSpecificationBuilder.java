package com.vinfast.rental_service.repository.specification;

import com.vinfast.rental_service.model.Car;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.vinfast.rental_service.repository.specification.SearchOperation.*;

public class CarSpecificationBuilder {
    private final List<SpecSearchCriteria> params;

    public CarSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public  CarSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix){
        return with(key, operation, value, prefix, suffix, null);
    }

    public CarSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix, String orPredicate){
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(searchOperation != null){
            if(searchOperation == EQUALITY){
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);
                if(startWithAsterisk && endWithAsterisk){
                    searchOperation = LIKE;
                }else if(startWithAsterisk) {
                    searchOperation = ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchOperation = STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(key, searchOperation, value, orPredicate));
        }
        return this;
    }

    public Specification<Car> build(){
        if(params.isEmpty()) return null;
        Specification<Car> result  = new CarSpecification(params.get(0));
        for(int i = 0; i < params.size(); i ++){
            result = params.get(i).isOrPredicate() ?
                    Specification.where(result).or(new CarSpecification(params.get(i))) : Specification.where(result).and(new CarSpecification(params.get(i)));
        }
        return result;
    }
}
