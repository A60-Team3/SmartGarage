package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleYear;

public interface VehicleYearService {

    VehicleYear getById(long id);

    VehicleYear getByYear(int year);

    VehicleYear create(VehicleYear vehicleYear);
}
