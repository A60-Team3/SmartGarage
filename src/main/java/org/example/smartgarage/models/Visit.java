package org.example.smartgarage.models;

import org.example.smartgarage.models.base.BaseEntity;
import org.example.smartgarage.models.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Visit extends BaseEntity {

    private LocalDate scheduleDate;

    private UserEntity client;

    private UserEntity clerk;

    private Status status;

    private Vehicle vehicle;

    private List<Service> services;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<EventLog> eventLogs;

    public Visit() {
    }

    public LocalDate getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(LocalDate scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public UserEntity getClient() {
        return client;
    }

    public void setClient(UserEntity client) {
        this.client = client;
    }

    public UserEntity getClerk() {
        return clerk;
    }

    public void setClerk(UserEntity clerk) {
        this.clerk = clerk;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
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

    public List<EventLog> getEventLogs() {
        return eventLogs;
    }

    public void setEventLogs(List<EventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Visit visit = (Visit) o;
        return Objects.equals(scheduleDate, visit.scheduleDate)
                && Objects.equals(client, visit.client)
                && Objects.equals(clerk, visit.clerk)
                && status == visit.status
                && Objects.equals(vehicle, visit.vehicle)
                && Objects.equals(createdAt, visit.createdAt)
                && Objects.equals(updatedAt, visit.updatedAt)
                && Objects.equals(eventLogs, visit.eventLogs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleDate, client, clerk, status, vehicle, createdAt, updatedAt, eventLogs);
    }
}
