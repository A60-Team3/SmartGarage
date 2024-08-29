package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

public class FilterSpecification<T> implements Specification<T> {
    private SearchCriteria filter;

    public FilterSpecification(SearchCriteria filter) {
        this.filter = filter;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<T> root, CriteriaQuery<?> query, @NonNull CriteriaBuilder criteriaBuilder) {
        Path<?> fieldPath = root.get(filter.getField());

        return null;
    }
}
