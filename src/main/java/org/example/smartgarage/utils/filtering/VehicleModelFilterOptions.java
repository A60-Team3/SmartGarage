package org.example.smartgarage.utils.filtering;

import java.util.Optional;

public class VehicleModelFilterOptions {
    private Optional<String> name;
    private Optional<Integer> year;
    private Optional<String> sortOrder;

    public VehicleModelFilterOptions(String name, Integer year, String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.year = Optional.ofNullable(year);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<Integer> getYear() {
        return year;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
