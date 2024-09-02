package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
@Entity
@Table(name = "vehicle_years")
public class VehicleYear extends BaseEntity {

    @Column(name = "year", nullable = false)
    private int year;

    @ManyToMany(mappedBy = "years")
    private Set<VehicleModel> models = new HashSet<>();

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
        VehicleYear that = (VehicleYear) o;
        return year == that.year;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(year);
    }
}
