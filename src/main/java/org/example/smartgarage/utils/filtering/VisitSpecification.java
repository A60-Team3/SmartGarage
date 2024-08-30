package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.example.smartgarage.exceptions.InvalidFilterArgumentException;
import org.example.smartgarage.models.Visit;
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

        visitFilterOptions.getBrandName().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .like(root.join("vehicle")
                                    .join("brandName")
                                    .get("brandName"),
                            "%" + value + "%"));
        });

        visitFilterOptions.getVehicleVin().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .like(root.join("vehicle").get("vin"), "%" + value + "%"));
        });

        visitFilterOptions.getVehicleRegistry().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .like(root.join("vehicle").get("licensePlate"), "%" + value + "%"));
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


        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}