package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "services")
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "service_type_id")
    private ServiceType serviceType;

    @Column(name = "added_on")
    @CreationTimestamp
    private LocalDateTime addedOn;

    @Column(name = "updated_on")
    @UpdateTimestamp
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visitId;

    public Order() {
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public LocalDateTime getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(LocalDateTime createdAt) {
        this.addedOn = createdAt;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedAt) {
        this.updatedOn = updatedAt;
    }

    public Visit getVisitId() {
        return visitId;
    }

    public void setVisitId(Visit visitId) {
        this.visitId = visitId;
    }

    @Override
    public String toString() {
        return String.format("[%s]", serviceType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(serviceType, order.serviceType)
                && Objects.equals(visitId, order.visitId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceType, visitId);
    }
}
