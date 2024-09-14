package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.repositories.contracts.VehicleRepository;
import org.example.smartgarage.services.contracts.VehicleService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.VehicleFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleSpecification;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final VisitService visitService;


    public VehicleServiceImpl(VehicleRepository vehicleRepository, VisitService visitService) {
        this.vehicleRepository = vehicleRepository;
        this.visitService = visitService;
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
        if (existingVehicle != null) {
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
        if (existingVehicle != null) {
            if (existingVehicle.getId() != repoVehicle.getId()) {
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

        VisitFilterOptions visitFilterOptions = new VisitFilterOptions(
                null, null, null, null, id,
                null, null, null, null,
                null, null, null, null);

        boolean empty = visitService.findAll(visitFilterOptions).isEmpty();

        if (empty) {
            vehicleRepository.delete(vehicle);
        } else {
            vehicle.setDeleted(true);
            vehicleRepository.saveAndFlush(vehicle);
        }

    }


}
