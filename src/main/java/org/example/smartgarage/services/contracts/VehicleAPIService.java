package org.example.smartgarage.services.contracts;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

public interface VehicleAPIService {
    void populateVehiclePropertiesDB() throws URISyntaxException, IOException;
}
