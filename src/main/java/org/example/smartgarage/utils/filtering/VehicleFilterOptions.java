package org.example.smartgarage.utils.filtering;

import java.util.Optional;

public class VehicleFilterOptions {
    private Optional<String> ownerPhone;
    private Optional<String> brandName;
    private Optional<String> vin;
    private Optional<String> licensePlate;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;
    private boolean isDeleted;

    public VehicleFilterOptions(String ownerPhone, String brandName,
                                String vin, String licensePlate,
                                String sortBy, String sortOrder) {
        this.ownerPhone = Optional.ofNullable(ownerPhone);
        this.brandName = Optional.ofNullable(brandName);
        this.vin = Optional.ofNullable(vin);
        this.licensePlate = Optional.ofNullable(licensePlate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getOwner() {
        return ownerPhone;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public Optional<String> getBrandName() {
        return brandName;
    }

    public Optional<String> getVin() {
        return vin;
    }

    public Optional<String> getLicensePlate() {
        return licensePlate;
    }

    public void setOwner(Optional<String> ownerPhone) {
        this.ownerPhone = ownerPhone;
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

    public Optional<String> getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(Optional<String> ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void removeInvalid() {
        if (ownerPhone.isPresent() && ownerPhone.get().trim().isEmpty() ){
            ownerPhone = Optional.empty();
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

        if (sortBy.isPresent() && sortBy.get().trim().isEmpty() ){
            sortBy = Optional.empty();
        }

        if (sortOrder.isPresent() && sortOrder.get().trim().isEmpty() ){
            sortOrder = Optional.empty();
        }
    }

}
