package org.example.smartgarage.utils.filtering;

import java.util.Optional;

public class VehicleFilterOptions {
    private Optional<String> owner;
    private Optional<String> brandName;
    private Optional<String> vehicleVin;
    private Optional<String> vehicleRegistry;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public VehicleFilterOptions(String owner, String brandName,
                                String vehicleVin, String vehicleRegistry,
                                String sortBy, String sortOrder) {
        this.owner = Optional.ofNullable(owner);
        this.brandName = Optional.ofNullable(brandName);
        this.vehicleVin = Optional.ofNullable(vehicleVin);
        this.vehicleRegistry = Optional.ofNullable(vehicleRegistry);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getOwner() {
        return owner;
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

    public Optional<String> getVehicleVin() {
        return vehicleVin;
    }

    public Optional<String> getVehicleRegistry() {
        return vehicleRegistry;
    }
}
