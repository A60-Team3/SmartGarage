package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.Vehicle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAll();

    Page<Vehicle> getAll(int offset, int pageSize);

    Vehicle getById(long id);
}
