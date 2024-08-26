package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
@Entity
@Table(name = "logs")
public class EventLog  extends BaseEntity {

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "visit_id")
    private Visit visitId;


    public EventLog() {
    }

    public EventLog(String description) {
        this(description, null);
    }

    public EventLog(String description, Visit visitId) {
        this.description = description;
        this.visitId = visitId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Visit getVisitId() {
        return visitId;
    }

    public void setVisitId(Visit visitId) {
        this.visitId = visitId;
    }

    @Override
    public String toString(){
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MMMM-y HH:mm:ss", Locale.ENGLISH);
        String formattedDate = getTimestamp().format(date);
        return String.format("[%s] %s%n", formattedDate, getDescription());
    }
}
