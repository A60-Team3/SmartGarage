package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VehicleModelSpecification implements Specification<VehicleModel> {

    private final VehicleModelFilterOptions vehicleModelFilterOptions;
    private final VehicleBrand vehicleBrand;

    public VehicleModelSpecification(VehicleModelFilterOptions vehicleModelFilterOptions, VehicleBrand vehicleBrand) {
        this.vehicleModelFilterOptions = vehicleModelFilterOptions;
        this.vehicleBrand = vehicleBrand;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<VehicleModel> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("brand"), vehicleBrand));

        vehicleModelFilterOptions.getName().ifPresent(value ->
                predicates.add(criteriaBuilder.like(root.get("modelName"), "%" + value + "%")));

        vehicleModelFilterOptions.getYear().ifPresent(value ->
                predicates.add(criteriaBuilder.equal(root.get("years").get("year"), value)));

        String sortOrder = vehicleModelFilterOptions.getSortOrder().orElse("asc");

        Order order = sortOrder.equalsIgnoreCase("desc")
                ? criteriaBuilder.desc(root.get("modelName"))
                : criteriaBuilder.asc(root.get("modelName"));

        query.orderBy(order);

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
