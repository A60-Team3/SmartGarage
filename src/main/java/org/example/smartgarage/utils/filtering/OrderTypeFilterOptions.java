package org.example.smartgarage.utils.filtering;

import java.math.BigDecimal;
import java.util.Optional;

public class OrderTypeFilterOptions {
    private Optional<String> name;
    private Optional<BigDecimal> minPrice;
    private Optional<BigDecimal> maxPrice;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public OrderTypeFilterOptions(String name, BigDecimal minPrice, BigDecimal maxPrice, String sortBy, String sortOrder) {
        this.name = Optional.ofNullable(name);
        //this.price = Optional.ofNullable(price);
        this.minPrice = Optional.ofNullable(minPrice);
        this.maxPrice = Optional.ofNullable(maxPrice);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getName() {
        return name;
    }

    public Optional<BigDecimal> getMinPrice() {
        return minPrice;
    }

    public Optional<BigDecimal> getMaxPrice() {
        return maxPrice;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }

    public void removeInvalid() {
        if (name.isPresent() && name.get().trim().isEmpty()) {
            name = Optional.empty();
        }

        if (minPrice.isPresent() && minPrice.get().toString().isEmpty()) {
            minPrice = Optional.empty();
        }

        if (maxPrice.isPresent() && maxPrice.get().toString().isEmpty()) {
            maxPrice = Optional.empty();
        }

        if (sortBy.isPresent() && sortBy.get().trim().isEmpty()) {
            sortBy = Optional.empty();
        }

        if (sortOrder.isPresent() && sortOrder.get().trim().isEmpty()) {
            sortOrder = Optional.empty();
        }
    }
}
