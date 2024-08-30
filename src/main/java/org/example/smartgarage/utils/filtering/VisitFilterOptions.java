package org.example.smartgarage.utils.filtering;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class VisitFilterOptions {

    private final Optional<Long> customerId;
    private final Optional<String> customerName;
    private final Optional<Long> clerkId;
    private final Optional<String> clerkName;
    private final Optional<TimeOperator> scheduleCondition;
    private final Optional<LocalDate> scheduleDateFrom;
    private final Optional<LocalDate> scheduleDateTo;
    private final Optional<String> brandName;
    private final Optional<String> vehicleVin;
    private final Optional<String> vehicleRegistry;
    private final Optional<TimeOperator> bookedCondition;
    private final Optional<LocalDateTime> bookedOn;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;


    public VisitFilterOptions(Long customerId, String customerName,
                              Long clerkId, String clerkName, TimeOperator scheduleCondition,
                              LocalDate scheduleDateFrom, LocalDate scheduleDateTo,
                              String brandName,
                              String vehicleVin, String vehicleRegistry,
                              TimeOperator bookedCondition, LocalDateTime bookedOn,
                              String sortBy, String sortOrder) {
        this.customerId = Optional.ofNullable(customerId);
        this.customerName = Optional.ofNullable(customerName);
        this.clerkId = Optional.ofNullable(clerkId);
        this.clerkName = Optional.ofNullable(clerkName);
        this.scheduleCondition = Optional.ofNullable(scheduleCondition);
        this.scheduleDateFrom = Optional.ofNullable(scheduleDateFrom);
        this.scheduleDateTo = Optional.ofNullable(scheduleDateTo);
        this.brandName = Optional.ofNullable(brandName);
        this.vehicleVin = Optional.ofNullable(vehicleVin);
        this.vehicleRegistry = Optional.ofNullable(vehicleRegistry);
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

    public Optional<String> getBrandName() {
        return brandName;
    }

    public Optional<String> getVehicleVin() {
        return vehicleVin;
    }

    public Optional<String> getVehicleRegistry() {
        return vehicleRegistry;
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
}
