package org.example.smartgarage.models;

import org.example.smartgarage.models.base.BaseEntity;

import java.time.LocalDateTime;
import java.util.Objects;

public class Vehicle extends BaseEntity {

    private String licencePlate;

    private String vin;

    private UserEntity owner;

    // subject to change
    private String brandName;

    private String modelName;

    private int yearOfCreation;
    // end of subject to change

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Vehicle() {
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(int yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return yearOfCreation == vehicle.yearOfCreation
                && Objects.equals(licencePlate, vehicle.licencePlate)
                && Objects.equals(vin, vehicle.vin)
                && Objects.equals(owner, vehicle.owner)
                && Objects.equals(brandName, vehicle.brandName)
                && Objects.equals(modelName, vehicle.modelName)
                && Objects.equals(createdAt, vehicle.createdAt)
                && Objects.equals(updatedAt, vehicle.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licencePlate, vin, owner, brandName, modelName, yearOfCreation, createdAt, updatedAt);
    }
}
