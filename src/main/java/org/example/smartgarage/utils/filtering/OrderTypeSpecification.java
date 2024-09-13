package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.example.smartgarage.models.ServiceType;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderTypeSpecification implements Specification<ServiceType> {

    private final OrderTypeFilterOptions orderTypeFilterOptions;

    public OrderTypeSpecification(OrderTypeFilterOptions orderTypeFilterOptions) {
        this.orderTypeFilterOptions = orderTypeFilterOptions;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<ServiceType> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        orderTypeFilterOptions.getName().ifPresent(value ->
                predicates.add(criteriaBuilder.like(root.get("serviceName"), "%" + value + "%")));

        Optional<BigDecimal> minPrice = orderTypeFilterOptions.getMinPrice();
        Optional<BigDecimal> maxPrice = orderTypeFilterOptions.getMaxPrice();

        if (minPrice.isPresent() && maxPrice.isPresent()) {
            predicates.add(criteriaBuilder.between(root.get("servicePrice"), minPrice.get(), maxPrice.get()));
        } else if (minPrice.isPresent()) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("servicePrice"), minPrice.get()));
        } else if (maxPrice.isPresent()) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("servicePrice"), maxPrice.get()));
        }

        if(orderTypeFilterOptions.getSortBy().isPresent()){
            String sortBy = orderTypeFilterOptions.getSortBy().get();
            String sortOrder = orderTypeFilterOptions.getSortOrder().orElse("asc");

            Order order;
            switch(sortBy){
                case "name":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("serviceName"))
                            : criteriaBuilder.asc(root.get("serviceName"));
                    break;
                case "price":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("servicePrice"))
                            : criteriaBuilder.asc(root.get("servicePrice"));
                    break;
                default:
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("id"))
                            : criteriaBuilder.asc(root.get("id"));
                    break;
            }

            query.orderBy(order);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
