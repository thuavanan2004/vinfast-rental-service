package com.vinfast.rental_service.repository.specification;

import com.vinfast.rental_service.model.RentalOrder;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.vinfast.rental_service.repository.specification.SearchOperation.*;

public class RentalOrderSpecificationBuilder {
    private final List<SpecSearchCriteria> params;

    public RentalOrderSpecificationBuilder() {
        this.params = new ArrayList<>();
    }
    public RentalOrderSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix){
        return with(null, key, operation, value, prefix, suffix);
    }

    public RentalOrderSpecificationBuilder with(String orPredicate, String key, String operation, String value, String prefix, String suffix){
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if(searchOperation != null){
            if(searchOperation == EQUALITY){
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);
                if(startWithAsterisk && endWithAsterisk){
                    searchOperation = LIKE;
                } else if (startWithAsterisk) {
                    searchOperation = ENDS_WITH;
                } else if(endWithAsterisk){
                    searchOperation = STARTS_WITH;
                }
            }
        }
        params.add(new SpecSearchCriteria(key, searchOperation, value, orPredicate));
        return  this;
    }

    public Specification<RentalOrder> build(){
        if(params.isEmpty()) return null;

        Specification<RentalOrder> results = new RentalOrderSpecification(params.get(0));
        for (int i = 0; i < params.size(); i ++){
            results = params.get(i).isOrPredicate() ?
                    Specification.where(results).or(new RentalOrderSpecification(params.get(i)))
                    : Specification.where(results).and(new RentalOrderSpecification(params.get(i)));
        }
        return results;
    }
}
