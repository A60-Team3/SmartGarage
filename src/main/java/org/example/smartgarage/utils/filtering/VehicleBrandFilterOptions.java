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

    public void removeInvalid() {
        if (name.isPresent() && name.get().trim().isEmpty()) {
            name = Optional.empty();
        }

        if (sortOrder.isPresent() && sortOrder.get().trim().isEmpty()) {
            sortOrder = Optional.empty();
        }
    }
}
