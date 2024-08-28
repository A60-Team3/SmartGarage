package org.example.smartgarage.dtos.response;

public record VehicleOutDTO(

        String licensePlate,

        String vin,

        String brandName,

        String modelName,

        int year,

        String ownerName,

        String addedOn,

        String updatedOn
) {
}
