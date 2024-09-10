package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import jakarta.persistence.criteria.Order;
import org.example.smartgarage.exceptions.InvalidFilterArgumentException;
import org.example.smartgarage.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class VisitSpecification implements Specification<Visit> {

    private final VisitFilterOptions visitFilterOptions;

    public VisitSpecification(VisitFilterOptions visitFilterOptions) {
        this.visitFilterOptions = visitFilterOptions;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Visit> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        visitFilterOptions.getCustomerId().ifPresent(value -> {
            predicates.add(criteriaBuilder.equal(root.join("client").get("id"), value));
        });

        visitFilterOptions.getClerkId().ifPresent(value -> {
            predicates.add(criteriaBuilder.equal(root.join("clerk").get("id"), value));
        });

        visitFilterOptions.getCustomerName().ifPresent(value -> {
            predicates.add(criteriaBuilder.or(criteriaBuilder
                            .like(root.join("client").get("firstName"), "%" + value + "%"),
                    criteriaBuilder
                            .like(root.join("client").get("lastName"), "%" + value + "%")));
        });

        visitFilterOptions.getClerkName().ifPresent(value -> {
            predicates.add(criteriaBuilder.or(criteriaBuilder
                            .like(root.join("clerk").get("firstName"), "%" + value + "%"),
                    criteriaBuilder
                            .like(root.join("clerk").get("lastName"), "%" + value + "%")));
        });

        visitFilterOptions.getVehicleId().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .equal(root.join("vehicle").get("id"), value));
        });

        visitFilterOptions.getOrders().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .in(root.join("orders").get("id")).value(value));
        });

        visitFilterOptions.getBookedOn().ifPresent(value -> {
            if (visitFilterOptions.getBookedCondition().isEmpty()) {
                throw new InvalidFilterArgumentException("Filter condition not valid");
            }

            Predicate predicate =
                    switch (visitFilterOptions.getBookedCondition().get().name()) {
                        case "EQUAL" -> criteriaBuilder.equal(root.get("bookedOn"), value);
                        case "NOT_EQUAL" -> criteriaBuilder.notEqual(root.get("bookedOn"), value);
                        case "GREATER_THAN" -> criteriaBuilder.greaterThan(root.get("bookedOn"), value);
                        case "LESS_THAN" -> criteriaBuilder.lessThan(root.get("bookedOn"), value);
                        case "GREATER_THAN_OR_EQUAL" ->
                                criteriaBuilder.greaterThanOrEqualTo(root.get("bookedOn"), value);
                        case "LESS_THAN_OR_EQUAL" -> criteriaBuilder.lessThanOrEqualTo(root.get("bookedOn"), value);
                        default -> throw new InvalidFilterArgumentException("Filter condition not valid");
                    };
            predicates.add(predicate);
        });

        visitFilterOptions.getScheduleDateFrom().ifPresent(value -> {
            if (visitFilterOptions.getScheduleCondition().isEmpty()) {
                throw new InvalidFilterArgumentException("Filter condition not valid");
            }

            Predicate predicate =
                    switch (visitFilterOptions.getScheduleCondition().get().name()) {
                        case "EQUAL" -> criteriaBuilder.equal(root.get("scheduleDate"), value);
                        case "NOT_EQUAL" -> criteriaBuilder.notEqual(root.get("scheduleDate"), value);
                        case "GREATER_THAN" -> criteriaBuilder.greaterThan(root.get("scheduleDate"), value);
                        case "LESS_THAN" -> criteriaBuilder.lessThan(root.get("scheduleDate"), value);
                        case "GREATER_THAN_OR_EQUAL" ->
                                criteriaBuilder.greaterThanOrEqualTo(root.get("scheduleDate"), value);
                        case "LESS_THAN_OR_EQUAL" -> criteriaBuilder.lessThanOrEqualTo(root.get("scheduleDate"), value);
                        case "BETWEEN" -> {

                            if (visitFilterOptions.getScheduleDateTo().isEmpty()) {
                                throw new InvalidFilterArgumentException("Filter condition not valid");
                            }

                            yield criteriaBuilder
                                    .between(root.get("scheduleDate"),
                                            value,
                                            visitFilterOptions.getScheduleDateTo().get());
                        }
                        default -> throw new InvalidFilterArgumentException("Filter condition not valid");
                    };
            predicates.add(predicate);
        });

        if (visitFilterOptions.getSortBy().isPresent()) {
            String sortBy = visitFilterOptions.getSortBy().get();
            String sortOrder = visitFilterOptions.getSortOrder().orElse("asc");
            Join<Vehicle, Vehicle> vehicleJoin = root.join("vehicle", JoinType.LEFT);
            Join<Vehicle, UserEntity> clerkJoin = root.join("clerk", JoinType.LEFT);
            Join<Vehicle, UserEntity> clientJoin = root.join("client", JoinType.LEFT);

            Order order;
            switch (sortBy) {
                case "clerk":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(clerkJoin.get("lastName"))
                            : criteriaBuilder.asc(clerkJoin.get("lastName"));
                    break;
                case "client":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(clientJoin.get("lastName"))
                            : criteriaBuilder.asc(clientJoin.get("lastName"));
                    break;
                case "brandName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("brandName").get("brandName"))
                            : criteriaBuilder.asc(vehicleJoin.join("brandName").get("brandName"));
                    break;
                case "modelName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("modelName").get("modelName"))
                            : criteriaBuilder.asc(vehicleJoin.join("modelName").get("modelName"));
                    break;
                case "yearOfCreation":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("year").get("year"))
                            : criteriaBuilder.asc(vehicleJoin.join("year").get("year"));
                    break;
                case "licensePlate":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("licensePlate"))
                            : criteriaBuilder.asc(vehicleJoin.join("licensePlate"));
                    break;
                case "vin":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("vin"))
                            : criteriaBuilder.asc(vehicleJoin.join("vin"));
                    break;
                case "updatedOn":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("updatedOn"))
                            : criteriaBuilder.asc(root.get("updatedOn"));
                    break;
                default:
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("scheduleDate"))
                            : criteriaBuilder.asc(root.get("scheduleDate"));
                    break;
            }

            query.orderBy(order);
        }


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}