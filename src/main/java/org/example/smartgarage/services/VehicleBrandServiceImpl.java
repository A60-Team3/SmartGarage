package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.repositories.contracts.VehicleBrandRepository;
import org.example.smartgarage.services.contracts.VehicleBrandService;
import org.example.smartgarage.utils.filtering.VehicleBrandFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleBrandSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class VehicleBrandServiceImpl implements VehicleBrandService {

    private final VehicleBrandRepository vehicleBrandRepository;

    @Autowired
    public VehicleBrandServiceImpl(VehicleBrandRepository vehicleBrandRepository) {
        this.vehicleBrandRepository = vehicleBrandRepository;
    }

    @Override
    public List<VehicleBrand> getAll() {
        List<VehicleBrand> brands = vehicleBrandRepository.findAll();
        List<VehicleBrand> sorted = brands.stream().sorted(Comparator.comparing(VehicleBrand::getBrandName)).toList();
        return sorted;
    }

    @Override
    public Page<VehicleBrand> getAll(int offset, int pageSize, VehicleBrandFilterOptions vehicleBrandFilterOptions) {
        VehicleBrandSpecification vehicleBrandSpecification = new VehicleBrandSpecification(vehicleBrandFilterOptions);
        Pageable pageable = PageRequest.of(offset, pageSize);
        return vehicleBrandRepository.findAll(vehicleBrandSpecification, pageable);
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
