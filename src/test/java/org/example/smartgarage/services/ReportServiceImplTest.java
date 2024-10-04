package org.example.smartgarage.services;

import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.helpers.CreationHelper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @InjectMocks
    private ReportServiceImpl reportService;

    private UserEntity mockUser;

    @BeforeEach
    public void setUp(){
        mockUser = CreationHelper.createMockUser();
        ReflectionTestUtils.setField(reportService, "MASTER_PASSWORD", "testPassword");
    }


    @Test
    void createVisitReport_Should_Return_EmptyByteArray_When_NoVisits() throws IOException {
        byte[] visitReport = reportService.createVisitReport(Collections.emptyList(), mockUser);

        Assertions.assertEquals(0, visitReport.length);
    }

    @Test
    void createVisitReport_Should_Return_ByteArray_When_VisitsExists() throws IOException {
        ServiceType serviceType = new ServiceType();
        serviceType.setServiceName("engine");
        serviceType.setServicePrice(BigDecimal.ONE);

        VehicleOutDTO vehicle = new VehicleOutDTO(1, "XXXXXXXX", "XXXXXXXXX",
                "Audi", "80", 1986, "John", "monday", "tuesday");


        VisitOutDto visit = new VisitOutDto(
                "today", "John", "Sam", vehicle,
                List.of(serviceType), "done",
                List.of("Received yesterday"), BigDecimal.ONE, "BGN");

        byte[] visitReport = reportService.createVisitReport(List.of(visit, visit), mockUser);

        Assertions.assertNotNull(visitReport);
        Assertions.assertTrue(visitReport.length > 0);
    }

    @Test
    public void testCreateQuotation_ShouldReturnByteArray() throws IOException {
        ServiceType serviceType = new ServiceType();
        serviceType.setServiceName("engine");
        serviceType.setServicePrice(BigDecimal.ONE);

        BigDecimal totalCost = new BigDecimal("500.00");
        double exchangeRate = 1.95;

        ByteArrayOutputStream result = reportService.createQuotation(
                mockUser,
                LocalDate.now(),
                CurrencyCode.USD,
                "Toyota",
                "Corolla",
                2021,
                List.of(serviceType),
                totalCost,
                exchangeRate
        );

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }

    @Test
    public void testCreateQuotation_ShouldHandleMissingCarBrand() throws IOException {
        List<ServiceType> mockServices = Collections.emptyList();
        BigDecimal totalCost = new BigDecimal("500.00");
        double exchangeRate = 1.95;

        ByteArrayOutputStream result = reportService.createQuotation(
                mockUser,
                LocalDate.now(),
                CurrencyCode.USD,
                null,  // No car brand
                "Corolla",
                2021,
                mockServices,
                totalCost,
                exchangeRate
        );

        assertNotNull(result);
        assertTrue(result.size() > 0);
    }
}