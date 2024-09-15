package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.utils.filtering.VehicleBrandFilterOptions;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleBrandService {

    List<VehicleBrand> getAll();

    Page<VehicleBrand> getAll(int offset, int pageSize, VehicleBrandFilterOptions vehicleBrandFilterOptions);

    VehicleBrand getById(long id);

    VehicleBrand getByName(String brandName);

    VehicleBrand create(VehicleBrand vehicleBrand);

}
