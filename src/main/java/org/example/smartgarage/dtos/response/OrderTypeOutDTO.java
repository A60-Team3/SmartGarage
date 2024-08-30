package org.example.smartgarage.dtos.response;

import java.math.BigDecimal;

public record OrderTypeOutDTO(

        String serviceName,

        BigDecimal servicePrice
) {
}
