package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.utils.filtering.VehicleModelFilterOptions;
import org.springframework.data.domain.Page;

import java.util.Optional;
import java.util.Set;

public interface VehicleModelService {

    VehicleModel getById(long id);

    VehicleModel getByName(String modelName);

    Optional<VehicleModel> findForQuotation(String modelName, String carBrand, int carYear);

    Page<VehicleModel> getByBrand(VehicleBrand brand, int offset, int pageSize, VehicleModelFilterOptions vehicleModelFilterOptions);

    VehicleModel create(VehicleModel vehicleModel);

    void save(VehicleModel vehicleModel);

    void saveAll(Set<VehicleModel> models);
}
