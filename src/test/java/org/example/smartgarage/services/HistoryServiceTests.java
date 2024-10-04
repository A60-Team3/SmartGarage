package org.example.smartgarage.services;

import org.example.smartgarage.helpers.CreationHelper;
import org.example.smartgarage.models.EventLog;
import org.example.smartgarage.repositories.contracts.HistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class HistoryServiceTests {

    @InjectMocks
    private HistoryServiceImpl historyService;

    @Mock
    private HistoryRepository historyRepository;

    @Test
    public void save_Should_CallRepository(){
        historyService.save(CreationHelper.createMockEventLog());
        Mockito.verify(historyRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(EventLog.class));
    }
}
