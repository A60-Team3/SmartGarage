package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleYear;

import java.util.List;

public interface VehicleYearService {

    List<VehicleYear> getAll();

    VehicleYear getById(long id);

    VehicleYear getByYear(int year);

    VehicleYear create(VehicleYear vehicleYear);
}
