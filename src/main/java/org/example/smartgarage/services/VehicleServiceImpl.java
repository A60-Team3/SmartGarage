package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.repositories.contracts.VehicleRepository;
import org.example.smartgarage.services.contracts.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleServiceImpl(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Vehicle> getAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public Page<Vehicle> getAll(int offset, int pageSize) {
        return vehicleRepository.findAll(PageRequest.of(offset, pageSize));
    }

    @Override
    public Vehicle getById(long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
    }

    @Override
    public Vehicle create(Vehicle vehicle, UserEntity user) {

        Vehicle existingVehicle = vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin());
        if (existingVehicle != null){
            throw new EntityDuplicateException("Vehicle already exists.");
        }
        vehicle.setClerk(user);
        vehicleRepository.save(vehicle);
        return vehicleRepository.findVehicleByLicensePlate(vehicle.getLicensePlate());

    }

    @Override
    public Vehicle update(long id, Vehicle vehicle, UserEntity user) {
        Vehicle repoVehicle = vehicleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
        Vehicle existingVehicle = vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin());
        if (existingVehicle != null){
            if (existingVehicle.getId() != repoVehicle.getId()){
                throw new EntityDuplicateException("Vehicle already exists");
            }
        }

        repoVehicle.setLicensePlate(vehicle.getLicensePlate());
        repoVehicle.setVin(vehicle.getVin());
        repoVehicle.setBrandName(vehicle.getBrandName());
        repoVehicle.setModelName(vehicle.getModelName());
        repoVehicle.setYearOfCreation(vehicle.getYearOfCreation());
        repoVehicle.setOwner(vehicle.getOwner());

        vehicleRepository.save(repoVehicle);
        return vehicleRepository.findById(id).get();
    }

    @Override
    public void delete(long id) {
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));

        vehicleRepository.delete(vehicle);
    }


}
