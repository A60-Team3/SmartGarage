package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.repositories.contracts.VehicleBrandRepository;
import org.example.smartgarage.utils.filtering.VehicleBrandFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleBrandSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;


import java.util.Optional;

import static org.example.smartgarage.helpers.CreationHelper.*;

@ExtendWith(MockitoExtension.class)
public class VehicleBrandServiceTests {

    @InjectMocks
    private VehicleBrandServiceImpl vehicleBrandService;

    @Mock
    private VehicleBrandRepository vehicleBrandRepository;

    private VehicleBrand brand;


    @BeforeEach
    public void setup() {
        brand = createMockBrand();
    }

    @Test
    public void getAll_Should_CallRepository() {
        int offset = 0;
        int pageSize = 10;
        VehicleBrandFilterOptions filterOptions = new VehicleBrandFilterOptions(null, null);

        vehicleBrandService.getAll(offset, pageSize, filterOptions);

        Mockito.verify(vehicleBrandRepository, Mockito.times(1))
                .findAll(Mockito.any(VehicleBrandSpecification.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getById_Should_ReturnBrand_When_IdIsValid() {
        Mockito.when(vehicleBrandRepository.findById(brand.getId()))
                .thenReturn(Optional.of(brand));

        VehicleBrand result = vehicleBrandService.getById(brand.getId());

        Assertions.assertEquals(brand, result);
    }

    @Test
    public void getById_Should_Throw_When_BrandNotFound() {
        Mockito.when(vehicleBrandRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleBrandService.getById(Mockito.anyLong()));
    }

    @Test
    public void getByName_Should_ReturnBrand_When_NameIsValid() {
        Mockito.when(vehicleBrandRepository.findByBrandNameIgnoreCase(brand.getBrandName()))
                .thenReturn(Optional.of(brand));

        VehicleBrand result = vehicleBrandService.getByName(brand.getBrandName());

        Assertions.assertEquals(brand, result);
    }

    @Test
    public void getByName_Should_CallCreateMethod_When_BrandNotFound() {
        VehicleBrand newBrand = createMockBrand();
        newBrand.setBrandName(brand.getBrandName());

        Mockito.when(vehicleBrandRepository.findByBrandNameIgnoreCase(brand.getBrandName()))
                .thenReturn(Optional.empty());
        Mockito.when(vehicleBrandRepository.saveAndFlush(Mockito.any(VehicleBrand.class)))
                .thenReturn(newBrand);

        vehicleBrandService.getByName(brand.getBrandName());


        Mockito.verify(vehicleBrandRepository, Mockito.times(2))
                .findByBrandNameIgnoreCase(brand.getBrandName());
        Mockito.verify(vehicleBrandRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(VehicleBrand.class));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExists(){
        Mockito.when(vehicleBrandRepository.findByBrandNameIgnoreCase(brand.getBrandName()))
                .thenReturn(Optional.of(brand));

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> vehicleBrandService.create(brand));
    }
}
