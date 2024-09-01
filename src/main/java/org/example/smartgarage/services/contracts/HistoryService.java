package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.EventLog;

public interface HistoryService {
    EventLog save(EventLog eventLog);
}
