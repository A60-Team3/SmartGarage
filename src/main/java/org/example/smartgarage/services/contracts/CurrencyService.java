package org.example.smartgarage.services.contracts;

import java.io.IOException;

public interface CurrencyService {
    double getConversionRate(String exchangeCurrency) throws IOException;
}
