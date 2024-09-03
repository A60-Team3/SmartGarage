package org.example.smartgarage.utils.filtering;

import java.util.Optional;

public class VehicleBrandFilterOptions {
    private Optional<String> name;
    private Optional<String> sortOrder;

    public VehicleBrandFilterOptions(String name, String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
