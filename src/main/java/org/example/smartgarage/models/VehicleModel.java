package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vehicle_models")
public class VehicleModel extends BaseEntity {

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "production_years_models",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "year_id", referencedColumnName = "id"))
    private Set<VehicleYear> years = new HashSet<>();

    @ManyToOne
    @JoinTable(name = "models_brands",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id", referencedColumnName = "id"))
    private VehicleBrand brand;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Set<VehicleYear> getYears() {
        return years;
    }

    public void setYears(Set<VehicleYear> years) {
        this.years = years;
    }

    public VehicleBrand getBrand() {
        return brand;
    }

    public void setBrand(VehicleBrand brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleModel that = (VehicleModel) o;
        return Objects.equals(modelName, that.modelName) &&
                years.containsAll(that.years) &&
                Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelName, years, brand);
    }
}
