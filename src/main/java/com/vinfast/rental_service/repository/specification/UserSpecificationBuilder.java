package com.vinfast.rental_service.repository.specification;

import com.vinfast.rental_service.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.parameters.P;

import java.util.ArrayList;
import java.util.List;

import static com.vinfast.rental_service.repository.specification.SearchOperation.*;

public class UserSpecificationBuilder {
    private final List<SpecSearchCriteria> params;

    public UserSpecificationBuilder() {
        this.params = new ArrayList<>();
    }

    public  UserSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix){
        return with(key, operation, value, prefix, suffix, null);
    }

    public UserSpecificationBuilder with(String key, String operation, String value, String prefix, String suffix, String orPredicate){
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

    public Specification<User> build(){
        if(params.isEmpty()) return null;
        Specification<User> result  = new UserSpecification(params.get(0));
        for(int i = 0; i < params.size(); i ++){
            result = params.get(i).isOrPredicate() ?
                    Specification.where(result).or(new UserSpecification(params.get(i))) : Specification.where(result).and(new UserSpecification(params.get(i)));
        }
        return result;
    }
}
