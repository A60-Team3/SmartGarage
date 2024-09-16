package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record VisitInDto(
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        @FutureOrPresent
        LocalDate bookedDate,
        @Positive(message = "No such customer. Must be positive.")
        Long customerId,
        @Positive(message = "No such vehicle. Must be positive.")
        Long vehicleId
        ) {
}
