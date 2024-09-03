package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.example.smartgarage.models.VehicleBrand;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VehicleBrandSpecification implements Specification<VehicleBrand> {

    private final VehicleBrandFilterOptions vehicleBrandFilterOptions;

    public VehicleBrandSpecification(VehicleBrandFilterOptions vehicleBrandFilterOptions) {
        this.vehicleBrandFilterOptions = vehicleBrandFilterOptions;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<VehicleBrand> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        vehicleBrandFilterOptions.getName().ifPresent(value ->
                predicates.add(criteriaBuilder.like(root.get("brandName"), "%" + value + "%")));

        String sortOrder = vehicleBrandFilterOptions.getSortOrder().orElse("asc");

        Order order = sortOrder.equalsIgnoreCase("desc")
                ? criteriaBuilder.desc(root.get("brandName"))
                : criteriaBuilder.asc(root.get("brandName"));

        query.orderBy(order);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
