package org.example.smartgarage.services;

import org.example.smartgarage.services.contracts.ReportService;
import org.example.smartgarage.services.contracts.VehicleModelService;
import org.example.smartgarage.services.contracts.VisitService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuotationServiceTests {

    @InjectMocks
    private QuotationServiceImpl quotationService;

    @Mock
    private VehicleModelService modelService;
    @Mock
    private VisitService visitService;
    @Mock
    private ReportService reportService;
}
