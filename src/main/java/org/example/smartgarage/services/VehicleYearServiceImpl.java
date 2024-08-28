package org.example.smartgarage.services;

import org.example.smartgarage.models.VehicleYear;
import org.example.smartgarage.repositories.contracts.VehicleYearRepository;
import org.example.smartgarage.services.contracts.VehicleYearService;
import org.springframework.stereotype.Service;

@Service
public class VehicleYearServiceImpl implements VehicleYearService {

    private final VehicleYearRepository vehicleYearRepository;

    public VehicleYearServiceImpl(VehicleYearRepository vehicleYearRepository) {
        this.vehicleYearRepository = vehicleYearRepository;
    }

    @Override
    public VehicleYear getById(long id) {
        return vehicleYearRepository.findById(id).orElseThrow();
    }
}
