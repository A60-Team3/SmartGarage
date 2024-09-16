package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderInDTO(

        @NotNull(message = "Order type ID should not be null")
        @Positive(message = "Order type ID should be positive")
        Long serviceTypeId

) {
}
