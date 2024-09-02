package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long>, JpaSpecificationExecutor<Vehicle> {
    Vehicle findVehicleByLicensePlate(String licensePlate);
    Vehicle findVehicleByLicensePlateOrVin(String licensePlate, String vin);
}
