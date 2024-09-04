package org.example.smartgarage.services;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.example.smartgarage.exceptions.ConversionRequestException;
import org.example.smartgarage.models.enums.CurrencyCode;
import org.example.smartgarage.services.contracts.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class CurrencyServiceImpl implements CurrencyService {
    private static final String BASE_CURRENCY = CurrencyCode.BGN.toString();

    @Value("${exchange.rate.api.key}")
    private String exchangeRateApiKey;
    @Value("${exchange.rate.api.base-url}")
    private String exchangeRateApiUrl;

    @Override
    public double getConversionRate(CurrencyCode exchangeCurrency) throws IOException {
        // Setting URL
        String url_str = getFullApiURL(exchangeCurrency.toString());

        // Making Request
        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        // Convert to JSON
        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonObject = root.getAsJsonObject();

        // Accessing object
        String req_result = jsonObject.get("result").getAsString();

        if (req_result.equals("error")) {
            String errorMessage = jsonObject.get("error-type").getAsString();
            throw new ConversionRequestException(errorMessage);
        }

        return jsonObject.get("conversion_rate").getAsDouble();
    }

    private String getFullApiURL(String exchangeCurrency) {
        return exchangeRateApiUrl
                + exchangeRateApiKey
                + "/pair/"
                + BASE_CURRENCY
                + "/"
                + exchangeCurrency;
    }
}

