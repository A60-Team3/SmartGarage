package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.VehicleYear;
import org.example.smartgarage.repositories.contracts.VehicleYearRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.example.smartgarage.helpers.CreationHelper.createMockYear;

@ExtendWith(MockitoExtension.class)
public class VehicleYearServiceTests {

    @InjectMocks
    private VehicleYearServiceImpl vehicleYearService;

    @Mock
    private VehicleYearRepository vehicleYearRepository;

    private VehicleYear year;


    @BeforeEach
    public void setup(){
        year = createMockYear();
    }

    @Test
    public void getAll_Should_CallRepository_WithoutFilter() {
        Mockito.when(vehicleYearRepository.findAll())
                .thenReturn(List.of(year));

        Assertions.assertEquals(List.of(year),vehicleYearService.getAll());
        Mockito.verify(vehicleYearRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void getById_Should_ReturnYear_When_IdIsValid() {
        Mockito.when(vehicleYearRepository.findById(year.getId()))
                .thenReturn(Optional.of(year));

        VehicleYear result = vehicleYearService.getById(year.getId());

        Assertions.assertEquals(year, result);
    }

    @Test
    public void getById_Should_Throw_When_YearNotFound() {
        Mockito.when(vehicleYearRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> vehicleYearService.getById(Mockito.anyLong()));
    }

    @Test
    public void getByYear_Should_ReturnBrand_When_NameIsValid() {
        Mockito.when(vehicleYearRepository.findByYear(year.getYear()))
                .thenReturn(Optional.of(year));

        VehicleYear result = vehicleYearService.getByYear(year.getYear());

        Assertions.assertEquals(year, result);
    }

    @Test
    public void getByYear_Should_CallCreateMethod_When_YearNotFound() {
        VehicleYear newYear = createMockYear();
        newYear.setYear(year.getYear());

        Mockito.when(vehicleYearRepository.findByYear(year.getYear()))
                .thenReturn(Optional.empty());
        Mockito.when(vehicleYearRepository.saveAndFlush(Mockito.any(VehicleYear.class)))
                .thenReturn(newYear);

        vehicleYearService.getByYear(year.getYear());


        Mockito.verify(vehicleYearRepository, Mockito.times(2))
                .findByYear(year.getYear());
        Mockito.verify(vehicleYearRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(VehicleYear.class));
    }

    @Test
    public void create_Should_Throw_When_DuplicateExists(){
        Mockito.when(vehicleYearRepository.findByYear(year.getYear()))
                .thenReturn(Optional.of(year));

        Assertions.assertThrows(EntityDuplicateException.class,
                () -> vehicleYearService.create(year));
    }
}
