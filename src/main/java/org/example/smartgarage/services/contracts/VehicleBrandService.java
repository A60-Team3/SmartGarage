package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleBrand;

public interface VehicleBrandService {

    VehicleBrand getById(long id);

    VehicleBrand getByName(String brandName);

    VehicleBrand create(VehicleBrand vehicleBrand);

}
