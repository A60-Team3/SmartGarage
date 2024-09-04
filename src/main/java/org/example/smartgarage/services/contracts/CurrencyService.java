package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.enums.CurrencyCode;

import java.io.IOException;

public interface CurrencyService {
    double getConversionRate(CurrencyCode exchangeCurrency) throws IOException;
}
