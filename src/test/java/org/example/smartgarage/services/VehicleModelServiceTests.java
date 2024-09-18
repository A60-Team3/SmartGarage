package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.repositories.contracts.VehicleModelRepository;
import org.example.smartgarage.utils.filtering.VehicleModelFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleModelSpecification;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.example.smartgarage.helpers.CreationHelper.createMockBrand;
import static org.example.smartgarage.helpers.CreationHelper.createMockModel;

@ExtendWith(MockitoExtension.class)
public class VehicleModelServiceTests {

    @InjectMocks
    private VehicleModelServiceImpl vehicleModelService;

    @Mock
    private VehicleModelRepository vehicleModelRepository;

    private VehicleModel model;

    @BeforeEach
    public void setup(){
        model = createMockModel();
    }

    @Test
    public void getAll_Should_CallRepository_WithoutFilter() {
        Mockito.when(vehicleModelRepository.findAll())
                .thenReturn(List.of(model));

        Assertions.assertEquals(List.of(model),vehicleModelService.getAll());
        Mockito.verify(vehicleModelRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getById_Should_ReturnModel_When_IdIsValid() {
        Mockito.when(vehicleModelRepository.findById(model.getId()))
                .thenReturn(Optional.of(model));

        VehicleModel result = vehicleModelService.getById(model.getId());

        Assertions.assertEquals(model, result);
    }

    @Test
    public void getById_Should_Throw_When_ModelNotFound() {
        Mockito.when(vehicleModelRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleModelService.getById(Mockito.anyLong()));
    }

    @Test
    public void getByName_Should_ReturnModel_When_NameIsValid() {
        Mockito.when(vehicleModelRepository.findByModelNameIgnoreCase(model.getModelName()))
                .thenReturn(Optional.of(model));

        VehicleModel result = vehicleModelService.getByName(model.getModelName());

        Assertions.assertEquals(model, result);
    }

    @Test
    public void getByName_Should_CallCreateMethod_When_ModelNotFound() {
        VehicleModel newModel = createMockModel();
        newModel.setModelName(model.getModelName());

        Mockito.when(vehicleModelRepository.findByModelNameIgnoreCase(model.getModelName()))
                .thenReturn(Optional.empty());
        Mockito.when(vehicleModelRepository.saveAndFlush(Mockito.any(VehicleModel.class)))
                .thenReturn(newModel);

        vehicleModelService.getByName(model.getModelName());


        Mockito.verify(vehicleModelRepository, Mockito.times(2))
                .findByModelNameIgnoreCase(model.getModelName());
        Mockito.verify(vehicleModelRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(VehicleModel.class));
    }

    @Test
    public void getByBrand_Should_CallRepository(){
        int offset = 0;
        int pageSize = 10;
        VehicleModelFilterOptions filterOptions = new VehicleModelFilterOptions(null, null, null);
        VehicleBrand brand = createMockBrand();

        vehicleModelService.getByBrand(brand, offset, pageSize, filterOptions);

        Mockito.verify(vehicleModelRepository, Mockito.times(1))
                .findAll(Mockito.any(VehicleModelSpecification.class), Mockito.any(Pageable.class));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExists(){
        Mockito.when(vehicleModelRepository.findByModelNameIgnoreCase(model.getModelName()))
                .thenReturn(Optional.of(model));

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> vehicleModelService.create(model));
    }

    @Test
    public void save_Should_CallRepository(){
        vehicleModelService.save(model);
        Mockito.verify(vehicleModelRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(VehicleModel.class));
    }

    @Test
    public void saveAll_Should_CallRepository(){
        vehicleModelService.saveAll(Set.of(model));
        Mockito.verify(vehicleModelRepository, Mockito.times(1))
                .saveAll(Mockito.anyCollection());
    }

    @Test
    public void findForQuotation_Should_CallRepository(){
        vehicleModelService.findForQuotation("model", "brand", 2000);
        Mockito.verify(vehicleModelRepository, Mockito.times(1))
                .findByModelNameAndBrandBrandNameAndYearsYear
                        (Mockito.anyString(), Mockito.anyString(), Mockito.anyInt());
    }
}
