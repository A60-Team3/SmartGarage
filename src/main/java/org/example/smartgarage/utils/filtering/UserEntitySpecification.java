package org.example.smartgarage.utils.filtering;

import jakarta.persistence.criteria.*;
import org.example.smartgarage.exceptions.InvalidFilterArgumentException;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UserEntitySpecification implements Specification<UserEntity> {

    private final UserFilterOptions userFilterOptions;

    public UserEntitySpecification(UserFilterOptions userFilterOptions) {
        this.userFilterOptions = userFilterOptions;
    }


    @Override
    public Predicate toPredicate(@NonNull Root<UserEntity> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        userFilterOptions.getFirstName().ifPresent(value -> {
            predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + value + "%"));
        });

        userFilterOptions.getLastName().ifPresent(value -> {
            predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + value + "%"));
        });

        userFilterOptions.getEmail().ifPresent(value -> {
            predicates.add(criteriaBuilder.like(root.get("email"), "%" + value + "%"));
        });

        userFilterOptions.getUsername().ifPresent(value -> {
            predicates.add(criteriaBuilder.like(root.get("username"), "%" + value + "%"));
        });

        userFilterOptions.getPhoneNumber().ifPresent(value -> {
            predicates.add(criteriaBuilder.like(root.get("phoneNumber"), "%" + value + "%"));
        });

        userFilterOptions.getRegistered().ifPresent(value -> {
            if (userFilterOptions.getRegisteredCondition().isEmpty()) {
                throw new InvalidFilterArgumentException("Filter condition not valid");
            }

            Predicate predicate =
                    switch (userFilterOptions.getRegisteredCondition().get().name()) {
                        case "EQUAL" -> criteriaBuilder.equal(root.get("registered"), value);
                        case "NOT_EQUAL" -> criteriaBuilder.notEqual(root.get("registered"), value);
                        case "GREATER_THAN" -> criteriaBuilder.greaterThan(root.get("registered"), value);
                        case "LESS_THAN" -> criteriaBuilder.lessThan(root.get("registered"), value);
                        case "GREATER_THAN_OR_EQUAL" ->
                                criteriaBuilder.greaterThanOrEqualTo(root.get("registered"), value);
                        case "LESS_THAN_OR_EQUAL" -> criteriaBuilder.lessThanOrEqualTo(root.get("registered"), value);
                        default -> throw new InvalidFilterArgumentException("Filter condition not valid");
                    };
            predicates.add(predicate);
        });

        userFilterOptions.getIsActive().ifPresent(value -> {
            predicates.add(criteriaBuilder.equal(root.get("isActive"), value));
        });

        userFilterOptions.getAuthority().ifPresent(value -> {
            predicates.add(criteriaBuilder.in(root.join("roles").get("userRole")).value(value));
        });

        userFilterOptions.getBrandName().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .equal(root.join("vehicles")
                                    .join("brandName")
                                    .get("brandName"),
                            "%" + value + "%"));
        });

        userFilterOptions.getVin().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .like(root.join("vehicles").get("vin"), "%" + value + "%"));
        });

        userFilterOptions.getLicensePlate().ifPresent(value -> {
            predicates.add(criteriaBuilder
                    .like(root.join("vehicles").get("licensePlate"), "%" + value + "%"));
        });

        if (userFilterOptions.getSortBy().isPresent()) {
            String sortBy = userFilterOptions.getSortBy().get();
            String sortOrder = userFilterOptions.getSortOrder().orElse("asc");
            Join<UserEntity, Vehicle> vehicleJoin = root.join("vehicles", JoinType.LEFT);
            Join<UserEntity, Role> roleJoin = root.join("roles", JoinType.LEFT);

            Order order;
            switch (sortBy) {
                case "lastName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("lastName"))
                            : criteriaBuilder.asc(root.get("lastName"));
                    break;
                case "firstName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("firstName"))
                            : criteriaBuilder.asc(root.get("firstName"));
                    break;
                case "email":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("email"))
                            : criteriaBuilder.asc(root.get("email"));
                    break;
                case "phoneNumber":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("phoneNumber"))
                            : criteriaBuilder.asc(root.get("phoneNumber"));
                    break;
                case "brandName":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.join("brandName").get("brandName"))
                            : criteriaBuilder.asc(vehicleJoin.join("brandName").get("brandName"));
                    break;
                case "licensePlate":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.get("licensePlate"))
                            : criteriaBuilder.asc(vehicleJoin.get("licensePlate"));
                    break;
                case "vin":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.get("vin"))
                            : criteriaBuilder.asc(vehicleJoin.get("vin"));
                    break;
                case "authority":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(roleJoin.get("userRole"))
                            : criteriaBuilder.asc(roleJoin.get("userRole"));
                    break;
                case "updatedOn":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("registered"))
                            : criteriaBuilder.asc(root.get("registered"));
                    break;
                default:
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("username"))
                            : criteriaBuilder.asc(root.get("username"));
                    break;
            }

            query.orderBy(order);
        } else {
            String sortOrder = userFilterOptions.getSortOrder().orElse("desc");
            Order order = sortOrder.equalsIgnoreCase("desc")
                    ? criteriaBuilder.desc(root.get("username"))
                    : criteriaBuilder.asc(root.get("username"));
            query.orderBy(order);
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
