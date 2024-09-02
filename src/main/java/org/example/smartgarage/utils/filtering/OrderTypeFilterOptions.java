package org.example.smartgarage.utils.filtering;

import java.math.BigDecimal;
import java.util.Optional;

public class OrderTypeFilterOptions {
    private Optional<String> name;
    private Optional<BigDecimal> price;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public OrderTypeFilterOptions(String name, BigDecimal price, String sortBy, String sortOrder) {
        this.name = Optional.ofNullable(name);
        this.price = Optional.ofNullable(price);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<BigDecimal> getPrice() {
        return price;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
