package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.*;
import org.example.smartgarage.repositories.contracts.VehicleRepository;
import org.example.smartgarage.services.contracts.VehicleService;
import org.example.smartgarage.utils.filtering.VehicleFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public List<Vehicle> findFiltered(VehicleFilterOptions filterOptions) {
        VehicleSpecification specification = new VehicleSpecification(filterOptions);

        return vehicleRepository.findAll(specification);
    }

    @Override
    public Page<Vehicle> getAll(int offset, int pageSize, VehicleFilterOptions vehicleFilterOptions) {
        VehicleSpecification vehicleSpecification = new VehicleSpecification(vehicleFilterOptions);

        Pageable pageable = PageRequest.of(offset, pageSize);
        return vehicleRepository.findAll(vehicleSpecification, pageable);
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
        vehicle.getModelName().setBrand(vehicle.getBrandName());
        //vehicle.getYearOfCreation().getModels().add(vehicle.getModelName());
        vehicle.getModelName().getYears().add(vehicle.getYearOfCreation());

        vehicleRepository.save(vehicle);
        return vehicleRepository.findVehicleByLicensePlate(vehicle.getLicensePlate());

    }

    @Override
    public Vehicle update(long id, Vehicle vehicle, UserEntity user) {
        Vehicle repoVehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found"));
        Vehicle existingVehicle = vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin());
        if (existingVehicle != null){
            if (existingVehicle.getId() != repoVehicle.getId()){
                throw new EntityDuplicateException("Vehicle already exists");
            }
        }
        vehicle.getModelName().setBrand(vehicle.getBrandName());
        vehicle.getYearOfCreation().getModels().add(vehicle.getModelName());
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
