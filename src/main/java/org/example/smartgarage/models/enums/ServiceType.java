package org.example.smartgarage.models.enums;

public enum ServiceType {
    BRAKES("Change the brakes", 50);

    private final String description;
    private final double price;

    ServiceType(String description, double price) {
        this.description = description;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
