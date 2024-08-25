package org.example.smartgarage.models;

import org.example.smartgarage.models.base.BaseEntity;
import org.example.smartgarage.models.enums.ServiceType;

import java.time.LocalDateTime;

public class Service extends BaseEntity {

    private ServiceType serviceType;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Service() {
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
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

}
