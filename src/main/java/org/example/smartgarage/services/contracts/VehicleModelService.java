package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleModel;

public interface VehicleModelService {

    VehicleModel getById(long id);

    VehicleModel getByName(String modelName);

    VehicleModel create(VehicleModel vehicleModel);

    void save(VehicleModel vehicleModel);
}
