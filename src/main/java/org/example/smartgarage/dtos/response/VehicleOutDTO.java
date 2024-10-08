package org.example.smartgarage.dtos.response;

public record VehicleOutDTO(
        long id,

        String licensePlate,

        String vin,

        String brandName,

        String modelName,

        int year,

        String ownerName,

        String addedOn,

        String updatedOn
) {
    @Override
    public String toString() {
        return String.format("%s || %s || %d", brandName, modelName, year);
    }
}
