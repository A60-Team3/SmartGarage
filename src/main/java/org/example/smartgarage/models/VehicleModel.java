package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vehicle_models")
public class VehicleModel extends BaseEntity {

    @Column(name = "model_name", nullable = false)
    private String modelName;

    @ManyToMany(mappedBy = "models")
    private Set<VehicleYear> years;

    @ManyToMany
    @JoinTable(name = "models_brands",
            joinColumns = @JoinColumn(name = "model_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "brand_id", referencedColumnName = "id"))
    private Set<VehicleBrand> brands;

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

    public Set<VehicleBrand> getBrands() {
        return brands;
    }

    public void setBrands(Set<VehicleBrand> brands) {
        this.brands = brands;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleModel that = (VehicleModel) o;
        return Objects.equals(modelName, that.modelName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(modelName);
    }
}
