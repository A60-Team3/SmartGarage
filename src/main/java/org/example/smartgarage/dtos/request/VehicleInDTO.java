package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleInDTO(

        @NotNull(message = "License plate should not be empty")
        @Pattern(regexp = licensePlateRegex)
        String licensePlate,

        @NotNull(message = "VIN should not be empty")
        @Size(min = 17, max = 17)
        String vin,

        @Positive(message = "Brand ID should be positive")
        long brandId,

        @Positive(message = "Model ID should be positive")
        long modelId,

        @Positive(message = "Year ID should be positive")
        long yearId,

        @Positive(message = "Owner ID should be positive")
        long ownerId

) {
        public static final String licensePlateRegex = "^[АВЕКМНОРСТУХ]{1,2}\\d\\d\\d\\d[АВЕКМНОРСТУХ]{2}$";
}
