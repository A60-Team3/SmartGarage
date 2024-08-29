package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
public class Vehicle extends BaseEntity {

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "VIN", nullable = false)
    private String vin;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private VehicleBrand brandName;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private VehicleModel modelName;

    @ManyToOne
    @JoinColumn(name = "year_id", nullable = false)
    private VehicleYear yearOfCreation;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private UserEntity clerk;

    @Column(name = "added_on")
    @CreationTimestamp
    private LocalDate addedOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private LocalDate updatedOn;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    public Vehicle() {
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
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

    public UserEntity getClerk() {
        return clerk;
    }

    public void setClerk(UserEntity clerk) {
        this.clerk = clerk;
    }

    public VehicleBrand getBrandName() {
        return brandName;
    }

    public void setBrandName(VehicleBrand brandName) {
        this.brandName = brandName;
    }

    public VehicleModel getModelName() {
        return modelName;
    }

    public void setModelName(VehicleModel modelName) {
        this.modelName = modelName;
    }

    public VehicleYear getYearOfCreation() {
        return yearOfCreation;
    }

    public void setYearOfCreation(VehicleYear yearOfCreation) {
        this.yearOfCreation = yearOfCreation;
    }

    public LocalDate getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDate addedOn) {
        this.addedOn = addedOn;
    }

    public LocalDate getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDate updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(licensePlate, vehicle.licensePlate)
                || Objects.equals(vin, vehicle.vin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(licensePlate, vin);
    }
}
