package org.example.smartgarage.utils.filtering;
import org.example.smartgarage.models.Order;
import jakarta.persistence.criteria.*;
import org.example.smartgarage.exceptions.InvalidFilterArgumentException;
import org.example.smartgarage.models.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;

public class OrderSpecification implements Specification<Order> {

    private final OrderFilterOptions orderFilterOptions;
    private final UserEntity user;

    public OrderSpecification(OrderFilterOptions orderFilterOptions, UserEntity user) {
        this.orderFilterOptions = orderFilterOptions;
        this.user = user;
    }

    @Override
    public Predicate toPredicate(@NonNull Root<Order> root,
                                 CriteriaQuery<?> query,
                                 @NonNull CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(criteriaBuilder.equal(root.get("visitId").get("client"), user));
        orderFilterOptions.getVehicle().ifPresent(value ->
                predicates.add(criteriaBuilder.equal(root.join("visitId")
                        .join("vehicle")
                        .get("licensePlate"),
                        value)));

        orderFilterOptions.getDate().ifPresent(value -> {
            if (orderFilterOptions.getCondition().isEmpty()) {
                throw new InvalidFilterArgumentException("Filter condition not valid");
            }

            Predicate predicate =
                    switch (orderFilterOptions.getCondition().get().name()) {
                        case "EQUAL" -> criteriaBuilder.equal(root.get("addedOn"), value);
                        case "NOT_EQUAL" -> criteriaBuilder.notEqual(root.get("addedOn"), value);
                        case "GREATER_THAN" -> criteriaBuilder.greaterThan(root.get("addedOn"), value);
                        case "LESS_THAN" -> criteriaBuilder.lessThan(root.get("addedOn"), value);
                        case "GREATER_THAN_OR_EQUAL" ->
                                criteriaBuilder.greaterThanOrEqualTo(root.get("addedOn"), value);
                        case "LESS_THAN_OR_EQUAL" -> criteriaBuilder.lessThanOrEqualTo(root.get("addedOn"), value);
                        default -> throw new InvalidFilterArgumentException("Filter condition not valid");
                    };
            predicates.add(predicate);
        });

        if(orderFilterOptions.getSortBy().isPresent()){
            String sortBy = orderFilterOptions.getSortBy().get();
            String sortOrder = orderFilterOptions.getSortOrder().orElse("asc");
            Join<org.example.smartgarage.models.Order, ServiceType> orderTypeJoin = root.join("serviceType", JoinType.LEFT);
            Join<org.example.smartgarage.models.Order, Visit> visitJoin = root.join("visitId", JoinType.LEFT);
            Join<Visit, Vehicle> vehicleJoin = visitJoin.join("vehicle", JoinType.LEFT);
            jakarta.persistence.criteria.Order order;

            switch(sortBy){
                case "vehicle":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(vehicleJoin.get("licensePlate"))
                            : criteriaBuilder.asc(vehicleJoin.get("licensePlate"));
                    break;
                case "name":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(orderTypeJoin.get("serviceName"))
                            : criteriaBuilder.asc(orderTypeJoin.get("serviceName"));
                    break;
                case "price":
                    order = sortOrder.equalsIgnoreCase("desc")
                            ? criteriaBuilder.desc(root.get("price"))
                            : criteriaBuilder.asc(root.get("price"));
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
