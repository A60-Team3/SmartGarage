package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.Positive;

public record OrderInDTO(

        @Positive(message = "Order type ID should be positive")
        long serviceTypeId

) {
}
