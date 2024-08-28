package org.example.smartgarage.services;

import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.repositories.contracts.VehicleBrandRepository;
import org.example.smartgarage.services.contracts.VehicleBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository vehicleBrandRepository;

    @Autowired
    public VehicleBrandServiceImpl(VehicleBrandRepository vehicleBrandRepository) {
        this.vehicleBrandRepository = vehicleBrandRepository;
    }

    @Override
    public VehicleBrand getById(long id) {
        return vehicleBrandRepository.findById(id).orElseThrow();
    }
}
