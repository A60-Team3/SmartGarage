package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.example.smartgarage.models.enums.Status;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "visits")
public class Visit extends BaseEntity {

    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private UserEntity client;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private UserEntity clerk;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "visitId")
    private List<Service> services;

    @Column(name = "booked_on")
    @CreationTimestamp
    private LocalDateTime bookedOn;

    @Column(name = "updated_on")
    @CreationTimestamp
    private LocalDateTime updatedOn;

    @OneToMany(mappedBy = "visitId")
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

    public LocalDateTime getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(LocalDateTime bookedOn) {
        this.bookedOn = bookedOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
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
                && Objects.equals(vehicle, visit.vehicle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleDate, client, vehicle);
    }
}
