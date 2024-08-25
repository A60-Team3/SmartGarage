package org.example.smartgarage.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class EventLog {

    private String description;
    private LocalDateTime timestamp;

    public EventLog() {
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

    @Override
    public String toString(){
        DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MMMM-y HH:mm:ss", Locale.ENGLISH);
        String formattedDate = getTimestamp().format(date);
        return String.format("[%s] %s%n", formattedDate, getDescription());
    }
}
