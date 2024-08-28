package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
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


}
