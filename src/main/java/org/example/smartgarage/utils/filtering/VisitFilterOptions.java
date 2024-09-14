package org.example.smartgarage.utils.filtering;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class VisitFilterOptions {

    private Optional<Long> customerId;
    private Optional<String> customerName;
    private Optional<Long> clerkId;
    private Optional<String> clerkName;
    private Optional<Long> vehicleId;
    private Optional<List<Long>> orders;
    private Optional<TimeOperator> scheduleCondition;
    private Optional<LocalDate> scheduleDateFrom;
    private Optional<LocalDate> scheduleDateTo;
    private Optional<TimeOperator> bookedCondition;
    private Optional<LocalDate> bookedOn;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public VisitFilterOptions(Long customerId, String customerName,
                              Long clerkId, String clerkName, Long vehicleId, List<Long> orders, TimeOperator scheduleCondition,
                              LocalDate scheduleDateFrom, LocalDate scheduleDateTo,
                              TimeOperator bookedCondition, LocalDate bookedOn,
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

    public Optional<LocalDate> getBookedOn() {
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

    public void setCustomerId(Optional<Long> customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(Optional<String> customerName) {
        this.customerName = customerName;
    }

    public void setClerkId(Optional<Long> clerkId) {
        this.clerkId = clerkId;
    }

    public void setClerkName(Optional<String> clerkName) {
        this.clerkName = clerkName;
    }

    public void setVehicleId(Optional<Long> vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setOrders(Optional<List<Long>> orders) {
        this.orders = orders;
    }

    public void setScheduleCondition(Optional<TimeOperator> scheduleCondition) {
        this.scheduleCondition = scheduleCondition;
    }

    public void setScheduleDateFrom(Optional<LocalDate> scheduleDateFrom) {
        this.scheduleDateFrom = scheduleDateFrom;
    }

    public void setScheduleDateTo(Optional<LocalDate> scheduleDateTo) {
        this.scheduleDateTo = scheduleDateTo;
    }

    public void setBookedCondition(Optional<TimeOperator> bookedCondition) {
        this.bookedCondition = bookedCondition;
    }

    public void setBookedOn(Optional<LocalDate> bookedOn) {
        this.bookedOn = bookedOn;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void removeInvalid() {
        if (customerName.isPresent() && customerName.get().trim().isEmpty() ){
            customerName = Optional.empty();
        }

        if (clerkName.isPresent() && clerkName.get().trim().isEmpty() ){
            clerkName = Optional.empty();
        }

        if (sortBy.isPresent() && sortBy.get().trim().isEmpty() ){
            sortBy = Optional.empty();
        }

        if (sortOrder.isPresent() && sortOrder.get().trim().isEmpty() ){
            sortOrder = Optional.empty();
        }

        if (orders.isPresent() && orders.get().isEmpty() ){
            orders = Optional.empty();
        }
    }
}
