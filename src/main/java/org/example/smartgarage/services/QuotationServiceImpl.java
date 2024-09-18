package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.ConversionRequestException;
import org.example.smartgarage.exceptions.ReportCreationException;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.services.contracts.*;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class QuotationServiceImpl implements QuotationService {
    public static final int MAX_VISITS_PER_DAY = 6;
    private final VehicleModelService vehicleModelService;
    private final VisitService visitService;
    private final ReportService reportService;
    private final OrderTypeService orderTypeService;
    private final CurrencyServiceImpl currencyService;

    public QuotationServiceImpl(VehicleModelService vehicleModelService, VisitService visitService,
                                ReportService reportService, OrderTypeService orderTypeService, CurrencyServiceImpl currencyService) {
        this.vehicleModelService = vehicleModelService;
        this.visitService = visitService;
        this.reportService = reportService;
        this.orderTypeService = orderTypeService;
        this.currencyService = currencyService;
    }

    @Override
    public boolean confirmVehicle(String carBrand, String carModel, int carYear) {
        return vehicleModelService
                .findForQuotation(carModel, carBrand, carYear)
                .isPresent();
    }

    @Override
    public ByteArrayOutputStream prepareQuotation(UserEntity client, LocalDate desiredDate,
                                                  List<String> requiredServices, CurrencyCode desiredCurrency) {
        return prepareQuotation(client, desiredDate, null, null, null,
                requiredServices, desiredCurrency);
    }

    @Override
    public ByteArrayOutputStream prepareQuotation(UserEntity client, LocalDate desiredDate, String carBrand,
                                                  String carModel, Integer carYear, List<String> requiredServices,
                                                  CurrencyCode desiredCurrency) {

        while (true) {
            VisitFilterOptions visitFilterOptions =
                    new VisitFilterOptions(null, null, null, null,
                            null, null, TimeOperator.EQUAL, desiredDate,
                            null, null, null, null, null);

            List<Visit> bookedVisits = visitService.findAll(visitFilterOptions);

            if (bookedVisits.size() < MAX_VISITS_PER_DAY) {
                break;
            }

            desiredDate = desiredDate.plusDays(1);
        }

        List<ServiceType> services = requiredServices.stream()
                .map(orderTypeService::findByName)
                .toList();

        double exchangeRate;
        if (desiredCurrency.equals(CurrencyCode.BGN)) {
            exchangeRate = 1.0;
        } else {
            try {
                exchangeRate = currencyService.getConversionRate(desiredCurrency);
            } catch (IOException e) {
                throw new ConversionRequestException(e.getMessage());
            }
        }

        BigDecimal totalCost = services.stream()
                .map(serviceType -> serviceType.getServicePrice()
                        .multiply(BigDecimal.valueOf(exchangeRate)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        try {
            return reportService.createQuotation(client, desiredDate, desiredCurrency, carBrand,
                    carModel, carYear, services, totalCost, exchangeRate);
        } catch (IOException e) {
            throw new ReportCreationException(e.getMessage());
        }
    }
}
