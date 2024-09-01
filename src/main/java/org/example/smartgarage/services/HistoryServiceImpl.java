package org.example.smartgarage.services;

import org.example.smartgarage.models.EventLog;
import org.example.smartgarage.repositories.contracts.HistoryRepository;
import org.example.smartgarage.services.contracts.HistoryService;
import org.springframework.stereotype.Service;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public EventLog save(EventLog eventLog) {
        return historyRepository.saveAndFlush(eventLog);
    }
}
