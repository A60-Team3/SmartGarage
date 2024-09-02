package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
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
        return vehicleBrandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Brand", id));
    }

    @Override
    public VehicleBrand getByName(String brandName) {
        return vehicleBrandRepository.findByBrandNameIgnoreCase(brandName)
                .orElseGet(() -> {
            VehicleBrand newBrand = new VehicleBrand();
            newBrand.setBrandName(brandName);
            return create(newBrand);
        });
    }

    @Override
    public VehicleBrand create(VehicleBrand vehicleBrand) {
        VehicleBrand existingBrand = vehicleBrandRepository.findByBrandNameIgnoreCase(vehicleBrand.getBrandName())
                .orElse(null);

        if(existingBrand != null){
            throw new EntityDuplicateException("Brand already exists");
        }

        return vehicleBrandRepository.saveAndFlush(vehicleBrand);
    }
}
