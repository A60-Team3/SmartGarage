package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.utils.filtering.VehicleModelFilterOptions;
import org.springframework.data.domain.Page;

public interface VehicleModelService {

    VehicleModel getById(long id);

    VehicleModel getByName(String modelName);

    Page<VehicleModel> getByBrand(VehicleBrand brand, int offset, int pageSize, VehicleModelFilterOptions vehicleModelFilterOptions);

    VehicleModel create(VehicleModel vehicleModel);

    void save(VehicleModel vehicleModel);
}
