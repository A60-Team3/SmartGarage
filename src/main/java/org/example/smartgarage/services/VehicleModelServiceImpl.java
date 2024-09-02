package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
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
        return vehicleModelRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Model", id));
    }

    @Override
    public VehicleModel getByName(String modelName) {
        return vehicleModelRepository.findByModelNameIgnoreCase(modelName)
                .orElseGet(() -> {
                    VehicleModel newModel = new VehicleModel();
                    newModel.setModelName(modelName);
                    return create(newModel);
                });
    }

    @Override
    public VehicleModel create(VehicleModel vehicleModel) {
        VehicleModel existingModel = vehicleModelRepository.findByModelNameIgnoreCase(vehicleModel.getModelName())
                .orElse(null);

        if(existingModel != null){
            throw new EntityDuplicateException("Model already exists");
        }

        return vehicleModelRepository.saveAndFlush(vehicleModel);
    }

    @Override
    public void save(VehicleModel vehicleModel) {
        vehicleModelRepository.saveAndFlush(vehicleModel);
    }
}
