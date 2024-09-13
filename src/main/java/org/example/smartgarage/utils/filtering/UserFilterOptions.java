package org.example.smartgarage.utils.filtering;

import org.example.smartgarage.models.enums.UserRole;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UserFilterOptions {

    private Optional<String> firstName;
    private Optional<String> lastName;
    private Optional<String> email;
    private Optional<String> username;
    private Optional<TimeOperator> registeredCondition;
    private Optional<LocalDateTime> registered;
    private Optional<Boolean> isActive;
    private Optional<List<UserRole>> authority;
    private Optional<String> phoneNumber;
    private Optional<String> brandName;
    private Optional<String> vin;
    private Optional<String> licensePlate;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public UserFilterOptions(String firstName, String lastName, String email, String username,
                             TimeOperator registeredCondition, LocalDateTime registered,
                             Boolean isActive, List<UserRole> authority,
                             String phoneNumber, String brandName, String vin, String licensePlate, String sortBy, String sortOrder) {

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
        this.vin = Optional.ofNullable(vin);
        this.licensePlate = Optional.ofNullable(licensePlate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getFirstName() {
        return firstName;
    }

    public Optional<String> getLicensePlate() {
        return licensePlate;
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

    public Optional<String> getVin() {
        return vin;
    }

    public void setFirstName(Optional<String> firstName) {
        this.firstName = firstName;
    }

    public void setLastName(Optional<String> lastName) {
        this.lastName = lastName;
    }

    public void setEmail(Optional<String> email) {
        this.email = email;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public void setRegisteredCondition(Optional<TimeOperator> registeredCondition) {
        this.registeredCondition = registeredCondition;
    }

    public void setRegistered(Optional<LocalDateTime> registered) {
        this.registered = registered;
    }

    public void setIsActive(Optional<Boolean> isActive) {
        this.isActive = isActive;
    }

    public void setAuthority(Optional<List<UserRole>> authority) {
        this.authority = authority;
    }

    public void setPhoneNumber(Optional<String> phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setBrandName(Optional<String> brandName) {
        this.brandName = brandName;
    }

    public void setVin(Optional<String> vin) {
        this.vin = vin;
    }

    public void setLicensePlate(Optional<String> licensePlate) {
        this.licensePlate = licensePlate;
    }

    public void setSortBy(Optional<String> sortBy) {
        this.sortBy = sortBy;
    }

    public void setSortOrder(Optional<String> sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void removeInvalid() {
        if (firstName.isPresent() && firstName.get().trim().isEmpty() ){
            firstName = Optional.empty();
        }

        if (lastName.isPresent() && lastName.get().trim().isEmpty() ){
            lastName = Optional.empty();
        }

        if (email.isPresent() && email.get().trim().isEmpty() ){
            email = Optional.empty();
        }

        if (username.isPresent() && username.get().trim().isEmpty() ){
            username = Optional.empty();
        }

        if (brandName.isPresent() && brandName.get().trim().isEmpty() ){
            brandName = Optional.empty();
        }

        if (licensePlate.isPresent() && licensePlate.get().trim().isEmpty() ){
            licensePlate = Optional.empty();
        }

        if (vin.isPresent() && vin.get().trim().isEmpty() ){
            vin = Optional.empty();
        }

        if (phoneNumber.isPresent() && phoneNumber.get().trim().isEmpty() ){
            phoneNumber = Optional.empty();
        }

        if (sortBy.isPresent() && sortBy.get().trim().isEmpty() ){
            sortBy = Optional.empty();
        }

        if (sortOrder.isPresent() && sortOrder.get().trim().isEmpty() ){
            sortOrder = Optional.empty();
        }
    }
}
