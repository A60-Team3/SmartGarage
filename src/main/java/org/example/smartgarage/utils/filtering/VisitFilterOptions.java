package org.example.smartgarage.utils.filtering;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class VisitFilterOptions {

    private final Optional<Long> customerId;
    private final Optional<String> customerName;
    private final Optional<Long> clerkId;
    private final Optional<String> clerkName;
    private final Optional<Long> vehicleId;
    private final Optional<List<Long>> orders;
    private final Optional<TimeOperator> scheduleCondition;
    private final Optional<LocalDate> scheduleDateFrom;
    private final Optional<LocalDate> scheduleDateTo;
    private final Optional<TimeOperator> bookedCondition;
    private final Optional<LocalDateTime> bookedOn;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;


    public VisitFilterOptions(Long customerId, String customerName,
                              Long clerkId, String clerkName, Long vehicleId, List<Long> orders, TimeOperator scheduleCondition,
                              LocalDate scheduleDateFrom, LocalDate scheduleDateTo,
                              TimeOperator bookedCondition, LocalDateTime bookedOn,
                              String sortBy, String sortOrder) {
        this.customerId = Optional.ofNullable(customerId);
        this.customerName = Optional.ofNullable(customerName);
        this.clerkId = Optional.ofNullable(clerkId);
        this.clerkName = Optional.ofNullable(clerkName);
        this.vehicleId = Optional.ofNullable(vehicleId);
        this.orders = Optional.ofNullable(orders);
        this.scheduleCondition = Optional.ofNullable(scheduleCondition);
        this.scheduleDateFrom = Optional.ofNullable(scheduleDateFrom);
        this.scheduleDateTo = Optional.ofNullable(scheduleDateTo);
        this.bookedCondition = Optional.ofNullable(bookedCondition);
        this.bookedOn = Optional.ofNullable(bookedOn);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Long> getCustomerId() {
        return customerId;
    }

    public Optional<String> getCustomerName() {
        return customerName;
    }

    public Optional<Long> getClerkId() {
        return clerkId;
    }

    public Optional<String> getClerkName() {
        return clerkName;
    }

    public Optional<TimeOperator> getScheduleCondition() {
        return scheduleCondition;
    }

    public Optional<LocalDate> getScheduleDateFrom() {
        return scheduleDateFrom;
    }

    public Optional<LocalDate> getScheduleDateTo() {
        return scheduleDateTo;
    }

    public Optional<TimeOperator> getBookedCondition() {
        return bookedCondition;
    }

    public Optional<LocalDateTime> getBookedOn() {
        return bookedOn;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public Optional<Long> getVehicleId() {
        return vehicleId;
    }

    public Optional<List<Long>> getOrders() {
        return orders;
    }
}
