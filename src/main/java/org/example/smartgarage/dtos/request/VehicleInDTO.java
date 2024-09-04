package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.*;

public record VehicleInDTO(

        @NotNull(message = "License plate should not be empty")
        @Size(min = 6, max = 8, message = "Invalid license plate length")
        @Pattern(regexp = licensePlateRegex)
        String licensePlate,

        @NotNull(message = "VIN should not be empty")
        @Size(min = 17, max = 17, message = "VIN length must be exactly 17 chars")
        String vin,

        @NotNull(message = "Brand name should not be empty!")
        @Size(min = 2, max = 50, message = "Brand name should be between 2 and 50 symbols!")
        String brandName,

        @NotNull(message = "Model name should not be empty!")
        @Size(min = 2, max = 50, message = "Model name should be between 2 and 50 symbols!")
        String modelName,

        @Positive(message = "Year should be positive")
        @Min(value = 1886, message = "You think there were cars before 1886? Not in this universe.")
        int year,

        @Positive(message = "Owner ID should be positive")
        long ownerId

) {
    public static final String licensePlateRegex =
            "^(A|B|BT|BP|BH|BA|C|CA|CB|CC|CH|CO|CT|CM|CP|E|EA|EM|H|K|KH|M|P|PA|PB|PK|PP|T|X|XX|Y)\\d{4}(A|B|C|E|H|K|M|O|P|T|X|Y){1,2}$";
}
