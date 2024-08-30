package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderTypeInDTO(

        @NotNull(message = "Service name should not be empty!")
        String serviceName,

        @Positive(message = "Service price should be a positive number!")
        BigDecimal servicePrice
) {
}
