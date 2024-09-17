package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.repositories.contracts.VehicleRepository;
import org.example.smartgarage.utils.filtering.VehicleFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleSpecification;
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

import static org.example.smartgarage.helpers.CreationHelper.createMockEmployee;
import static org.example.smartgarage.helpers.CreationHelper.createMockVehicle;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTests {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    private Vehicle vehicle;
    private UserEntity clerk;

    @BeforeEach
    public void setup(){
        vehicle = createMockVehicle();
        clerk = createMockEmployee();
    }

    @Test
    public void getAll_Should_CallRepository(){
        int offset = 0;
        int pageSize = 10;
        VehicleFilterOptions vehicleFilterOptions = new VehicleFilterOptions(null,
                null,
                null,
                null,
                null,
                null);

        vehicleService.getAll(offset, pageSize, vehicleFilterOptions);

        Mockito.verify(vehicleRepository, Mockito.times(1))
                .findAll(Mockito.any(VehicleSpecification.class), Mockito.any(Pageable.class));
    }

    @Test
    public void getById_Should_ReturnVehicle_When_IdIsValid(){
        Mockito.when(vehicleRepository.findById(vehicle.getId()))
                .thenReturn(Optional.of(vehicle));
        Vehicle result = vehicleService.getById(vehicle.getId());

        Assertions.assertEquals(vehicle, result);
    }

    @Test
    public void getById_Should_Throw_When_VehicleNotFound(){
        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.getById(Mockito.anyLong()));
    }

    @Test
    public void create_Should_CreateVehicle(){
        Mockito.when(vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin()))
                .thenReturn(null);

        vehicleService.create(vehicle, clerk);

        Mockito.verify(vehicleRepository, Mockito.times(1))
                .save(vehicle);
    }

    @Test
    public void create_Should_Throw_When_DuplicateExists(){
        Mockito.when(vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin()))
                .thenReturn(vehicle);

        Assertions.assertThrows(EntityDuplicateException.class, () -> vehicleService.create(vehicle, clerk));
    }

    @Test
    public void update_Should_UpdateVehicle(){
        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(vehicle));
        Mockito.when(vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin()))
                .thenReturn(vehicle);

        vehicleService.update(vehicle.getId(), vehicle, clerk);

        Mockito.verify(vehicleRepository, Mockito.times(1))
                .save(vehicle);
    }

    @Test
    public void update_Should_Throw_When_VehicleNotFound(){
        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.update(vehicle.getId(), vehicle, clerk));
    }

    @Test
    public void update_Should_Throw_When_DuplicateExists(){
        Vehicle duplicate = createMockVehicle();
        duplicate.setId(2);

        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(vehicle));
        Mockito.when(vehicleRepository.findVehicleByLicensePlateOrVin(vehicle.getLicensePlate(), vehicle.getVin()))
                .thenReturn(duplicate);

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> vehicleService.update(vehicle.getId(), vehicle, clerk));
    }

    /*@Test
    public void delete_Should_DeleteVehicle(){
        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(vehicle));

        vehicleService.delete(vehicle.getId());

        Mockito.verify(vehicleRepository, Mockito.times(1))
                .delete(vehicle);
    }*/

    @Test
    public void delete_Should_Throw_When_VehicleNotFound(){
        Mockito.when(vehicleRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleService.delete(vehicle.getId()));
    }

}
