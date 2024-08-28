package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.springframework.data.domain.Page;

import java.util.List;

public interface VehicleService {

    List<Vehicle> getAll();

    Page<Vehicle> getAll(int offset, int pageSize);

    Vehicle getById(long id);

    Vehicle create(Vehicle vehicle, UserEntity user);

    Vehicle update(long id, Vehicle vehicle, UserEntity user);

    void delete(long id, UserEntity user);
}
