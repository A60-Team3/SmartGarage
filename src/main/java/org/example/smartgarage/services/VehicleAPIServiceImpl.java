package org.example.smartgarage.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.transaction.Transactional;
import org.example.smartgarage.dtos.request.VehiclesRequestDto;
import org.example.smartgarage.exceptions.VehicleRequestException;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.models.VehicleYear;
import org.example.smartgarage.services.contracts.VehicleAPIService;
import org.example.smartgarage.services.contracts.VehicleBrandService;
import org.example.smartgarage.services.contracts.VehicleModelService;
import org.example.smartgarage.services.contracts.VehicleYearService;
import org.springframework.aot.hint.TypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

@Service
public class VehicleAPIServiceImpl implements VehicleAPIService {

    private final static String API_URL = "https://public.opendatasoft.com/api/explore/v2.1/" +
            "catalog/datasets/all-vehicles-model/" +
            "exports/json?select=make%2C%20model%2C%20year&limit=-1&timezone=UTC&use_labels=true&epsg=4326";

    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final VehicleYearService vehicleYearService;


    public VehicleAPIServiceImpl(VehicleBrandService vehicleBrandService,
                                 VehicleModelService vehicleModelService,
                                 VehicleYearService vehicleYearService) {
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleModelService = vehicleModelService;
        this.vehicleYearService = vehicleYearService;

    }

    @Override
    public void populateVehiclePropertiesDB() throws URISyntaxException, IOException {
        URL url = new URL(API_URL);

        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonElement root = JsonParser.parseReader(new InputStreamReader((InputStream) request.getContent()));
        if (root.isJsonArray()){
            JsonArray jsonArray = root.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject object = jsonElement.getAsJsonObject();

                String make = object.get("make").getAsString();
                VehicleBrand brandName = vehicleBrandService.getByName(make);

                int year = object.get("year").getAsInt();
                VehicleYear productionYear = vehicleYearService.getByYear(year);

                String model = object.get("model").getAsString();
                VehicleModel vehicleModel = vehicleModelService.getByName(model);
                vehicleModel.setBrand(brandName);
                vehicleModel.getYears().add(productionYear);

                vehicleModelService.save(vehicleModel);
            }
        }



    }
}
