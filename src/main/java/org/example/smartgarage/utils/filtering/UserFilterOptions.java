package org.example.smartgarage.utils.filtering;

import org.example.smartgarage.models.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserFilterOptions {

    private final Optional<String> firstName;
    private final Optional<String> lastName;
    private final Optional<String> email;
    private final Optional<String> username;
    private final Optional<TimeOperator> registeredCondition;
    private final Optional<LocalDateTime> registered;
    private final Optional<Boolean> isActive;
    private final Optional<List<UserRole>> authority;
    private final Optional<String> phoneNumber;
    private final Optional<String> brandName;
    private final Optional<String> vehicleVin;
    private final Optional<String> vehicleRegistry;
    private final Optional<String> sortBy;
    private final Optional<String> sortOrder;

    public UserFilterOptions(String firstName, String lastName, String email, String username,
                             TimeOperator registeredCondition, LocalDateTime registered,
                             Boolean isActive, List<UserRole> authority,
                             String phoneNumber, String brandName, String vehicleVin, String vehicleRegistry, String sortBy, String sortOrder) {

        this.firstName = Optional.ofNullable(firstName);
        this.lastName = Optional.ofNullable(lastName);
        this.email = Optional.ofNullable(email);
        this.username = Optional.ofNullable(username);
        this.registeredCondition = Optional.ofNullable(registeredCondition);
        this.registered = Optional.ofNullable(registered);
        this.isActive = Optional.ofNullable(isActive);
        this.authority = Optional.ofNullable(authority);
        this.phoneNumber = Optional.ofNullable(phoneNumber);
        this.brandName = Optional.ofNullable(brandName);
        this.vehicleVin = Optional.ofNullable(vehicleVin);
        this.vehicleRegistry = Optional.ofNullable(vehicleRegistry);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getVehicleRegistry() {
        return vehicleRegistry;
    }

    public Optional<String> getLastName() {
        return lastName;
    }

    public Optional<String> getEmail() {
        return email;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public Optional<LocalDateTime> getRegistered() {
        return registered;
    }

    public Optional<Boolean> getIsActive() {
        return isActive;
    }

    public Optional<List<UserRole>> getAuthority() {
        return authority;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public Optional<TimeOperator> getRegisteredCondition() {
        return registeredCondition;
    }

    public Optional<String> getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getBrandName() {
        return brandName;
    }

    public Optional<String> getVehicleVin() {
        return vehicleVin;
    }
}
