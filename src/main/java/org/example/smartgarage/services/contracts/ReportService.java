package org.example.smartgarage.services.contracts;

import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.CurrencyCode;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    ByteArrayOutputStream createVisitReport(List<VisitOutDto> visits, UserEntity user) throws IOException;

    ByteArrayOutputStream createQuotation(UserEntity client, LocalDate desiredDate, CurrencyCode desiredCurrency,
                                          String carBrand, String carModel, Integer carYear, List<ServiceType> services, BigDecimal totalCost, double exchangeRate) throws IOException;
}
