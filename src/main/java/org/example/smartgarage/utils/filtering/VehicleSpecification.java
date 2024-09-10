package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.models.VehicleYear;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VehicleSpecification implements Specification<Vehicle> {

    private final VehicleFilterOptions vehicleFilterOptions;

    public VehicleSpecification(VehicleFilterOptions vehicleFilterOptions) {
        this.vehicleFilterOptions = vehicleFilterOptions;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Vehicle> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {


        List<Predicate> predicates = new ArrayList<>();

        vehicleFilterOptions.getOwner().ifPresent(value ->
                predicates.add(criteriaBuilder.equal(root.join("owner").get("phoneNumber"), value)));
        vehicleFilterOptions.getBrandName().ifPresent(value ->
                predicates.add(criteriaBuilder.equal(root.join("brandName").get("brandName"), value)));
        vehicleFilterOptions.getVehicleVin().ifPresent(value ->
                predicates.add(criteriaBuilder.like(root.get("vin"), "%"+value+"%")));
        vehicleFilterOptions.getVehicleRegistry().ifPresent(value ->
                predicates.add(criteriaBuilder.like(root.get("licensePlate"), "%"+value+"%")));

        if (vehicleFilterOptions.getSortBy().isPresent()) {
            String sortBy = vehicleFilterOptions.getSortBy().get();
            String sortOrder = vehicleFilterOptions.getSortOrder().orElse("asc");
            Join<Vehicle, VehicleBrand> brandJoin = root.join("brandName", JoinType.LEFT);
            Join<Vehicle, VehicleModel> modelJoin = root.join("modelName", JoinType.LEFT);
            Join<Vehicle, VehicleYear> yearJoin = root.join("yearOfCreation", JoinType.LEFT);

            Order order;
            switch (sortBy) {
                case "brandName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(brandJoin.get("brandName"))
                            : criteriaBuilder.asc(brandJoin.get("brandName"));
                    break;
                case "modelName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(modelJoin.get("modelName"))
                            : criteriaBuilder.asc(modelJoin.get("modelName"));
                    break;
                case "yearOfCreation":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(yearJoin.get("year"))
                            : criteriaBuilder.asc(yearJoin.get("year"));
                    break;
                case "licensePlate":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("licensePlate"))
                            : criteriaBuilder.asc(root.get("licensePlate"));
                    break;
                case "vin":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("vin"))
                            : criteriaBuilder.asc(root.get("vin"));
                    break;
                case "updatedOn":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("updatedOn"))
                            : criteriaBuilder.asc(root.get("updatedOn"));
                    break;
                default:
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("addedOn"))
                            : criteriaBuilder.asc(root.get("addedOn"));
                    break;
            }

            query.orderBy(order);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
