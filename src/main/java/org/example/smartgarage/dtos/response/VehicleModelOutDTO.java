package org.example.smartgarage.dtos.response;

import java.util.Set;

public record VehicleModelOutDTO(
        String modelName,
        Set<Integer> years
) {
}
