package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record VehicleInDTO(

        @NotNull(message = "License plate should not be empty")
        @Size(min = 6, max = 8, message = "Invalid license plate length")
        @Pattern(regexp = licensePlateRegex)
        String licensePlate,

        @NotNull(message = "VIN should not be empty")
        @Size(min = 17, max = 17, message = "VIN length must be exactly 17 chars")
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
        public static final String licensePlateRegex =
                "^(A|B|BT|BP|BH|BA|C|CA|CB|CC|CH|CO|CT|CM|CP|E|EA|EM|H|K|KH|M|P|PA|PB|PK|PP|T|X|XX|Y)\\d{4}(A|B|C|E|H|K|M|O|P|T|X|Y){1,2}$";
}
