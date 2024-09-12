package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.CurrencyCode;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

public interface QuotationService {
    boolean confirmVehicle(String carBrand, String carModel, int carYear);

    ByteArrayOutputStream prepareQuotation(UserEntity client, LocalDate desiredDate, String carBrand, String carModel, Integer carYear, List<String> requiredServices, CurrencyCode desiredCurrency);

    ByteArrayOutputStream prepareQuotation(UserEntity client, LocalDate desiredDate, List<String> requiredServices, CurrencyCode desiredCurrency);
}
