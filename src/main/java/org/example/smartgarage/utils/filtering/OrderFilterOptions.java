package org.example.smartgarage.utils.filtering;

import java.time.LocalDateTime;
import java.util.Optional;

public class OrderFilterOptions {
    private Optional<String> vehicle;
    private Optional<TimeOperator> condition;
    private Optional<LocalDateTime> date;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public OrderFilterOptions(String vehicle,
                              LocalDateTime date,
                              TimeOperator condition,
                              String sortBy,
                              String sortOrder) {
        this.vehicle = Optional.ofNullable(vehicle);
        this.condition = Optional.ofNullable(condition);
        this.date = Optional.ofNullable(date);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getVehicle() {
        return vehicle;
    }

    public Optional<TimeOperator> getCondition() {
        return condition;
    }

    public Optional<LocalDateTime> getDate() {
        return date;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
