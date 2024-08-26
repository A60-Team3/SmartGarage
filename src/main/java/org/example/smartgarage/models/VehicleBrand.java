package org.example.smartgarage.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import org.example.smartgarage.models.baseEntity.BaseEntity;

import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "vehicle_brands")
public class VehicleBrand extends BaseEntity {
    @Column(name = "brand_name", nullable = false)
    private String brandName;

    @ManyToMany(mappedBy = "brands")
    private Set<VehicleModel> models;

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public Set<VehicleModel> getModels() {
        return models;
    }

    public void setModels(Set<VehicleModel> models) {
        this.models = models;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VehicleBrand that = (VehicleBrand) o;
        return Objects.equals(brandName, that.brandName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(brandName);
    }
}
