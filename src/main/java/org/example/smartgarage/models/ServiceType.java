package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "service_types")
public class ServiceType extends BaseEntity {

    @Column(name = "service_name", unique = true, nullable = false)
    private String serviceName;
    @Column(name = "service_price", scale = 19, precision = 2, nullable = false)
    private BigDecimal servicePrice;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(BigDecimal servicePrice) {
        this.servicePrice = servicePrice;
    }

    @Override
    public String toString() {
        return String.format("Service: %s, Cost: %s",serviceName, servicePrice);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceType that = (ServiceType) o;
        return Objects.equals(serviceName, that.serviceName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serviceName);
    }
}
