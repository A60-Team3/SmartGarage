package org.example.smartgarage.utils.filtering;

import java.util.Optional;

public class VehicleFilterOptions {
    private Optional<String> owner;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public VehicleFilterOptions(String owner, String sortBy, String sortOrder) {
        this.owner = Optional.ofNullable(owner);
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
}
