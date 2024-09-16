package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record OrderTypeInDTO(

        @NotBlank(message = "Service name should not be empty!")
        String serviceName,

        @NotNull(message = "Service price should not be empty!")
        @Positive(message = "Service price should be a positive number!")
        BigDecimal servicePrice
) {
}
