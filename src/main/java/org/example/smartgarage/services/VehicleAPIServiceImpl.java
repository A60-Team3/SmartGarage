package org.example.smartgarage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.smartgarage.dtos.request.VehiclesRequestDto;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.models.VehicleYear;
import org.example.smartgarage.services.contracts.VehicleAPIService;
import org.example.smartgarage.services.contracts.VehicleBrandService;
import org.example.smartgarage.services.contracts.VehicleModelService;
import org.example.smartgarage.services.contracts.VehicleYearService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class VehicleAPIServiceImpl implements VehicleAPIService {

    private final static String API_BASE_URL = "https://public.opendatasoft.com/api/explore/" +
            "v2.1/catalog/datasets/all-vehicles-model/exports/json";

    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final VehicleYearService vehicleYearService;
    private final ObjectMapper mapper;


    public VehicleAPIServiceImpl(VehicleBrandService vehicleBrandService,
                                 VehicleModelService vehicleModelService,
                                 VehicleYearService vehicleYearService, ObjectMapper mapper) {
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleModelService = vehicleModelService;
        this.vehicleYearService = vehicleYearService;

        this.mapper = mapper;
    }

    @Override
    public void populateVehiclePropertiesDB() throws IOException {
        List<VehiclesRequestDto> vehicles = getVehicles();

        saveVehiclesToDatabase(vehicles);
    }

    private void saveVehiclesToDatabase(List<VehiclesRequestDto> vehicles) {
        int batchSize = 1000;
        Set<VehicleModel> models = new HashSet<>();

        for (VehiclesRequestDto vehicle : vehicles) {

            VehicleBrand brand = vehicleBrandService.getByName(vehicle.make());
            VehicleYear year = vehicleYearService.getByYear(Integer.parseInt(vehicle.year()));
            VehicleModel model = vehicleModelService.getByName(vehicle.model());
            model.setBrand(brand);
            model.getYears().add(year);

            models.add(model);

            if (models.size() == batchSize) {
                vehicleModelService.saveAll(models);
                models.clear();
            }
        }

        if (!models.isEmpty()) {
            vehicleModelService.saveAll(models);
        }
    }

    private List<VehiclesRequestDto> getVehicles() {
        RestClient carApiClient = RestClient.create();

        URI uri = UriComponentsBuilder.fromHttpUrl(API_BASE_URL)
                .queryParam("select", "make,model,year")
                .queryParam("limit", "-1")
                .build().encode().toUri();

        return carApiClient.get()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
