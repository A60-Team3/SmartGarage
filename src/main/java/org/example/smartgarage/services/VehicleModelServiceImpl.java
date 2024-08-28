package org.example.smartgarage.services;

import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.repositories.contracts.VehicleModelRepository;
import org.example.smartgarage.services.contracts.VehicleModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleModelServiceImpl implements VehicleModelService {

    private final VehicleModelRepository vehicleModelRepository;

    @Autowired
    public VehicleModelServiceImpl(VehicleModelRepository vehicleModelRepository) {
        this.vehicleModelRepository = vehicleModelRepository;
    }

    @Override
    public VehicleModel getById(long id) {
        return vehicleModelRepository.findById(id).orElseThrow();
    }
}
