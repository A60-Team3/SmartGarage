package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.VehicleYear;
import org.example.smartgarage.repositories.contracts.VehicleYearRepository;
import org.example.smartgarage.services.contracts.VehicleYearService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class VehicleYearServiceImpl implements VehicleYearService {

    private final VehicleYearRepository vehicleYearRepository;

    public VehicleYearServiceImpl(VehicleYearRepository vehicleYearRepository) {
        this.vehicleYearRepository = vehicleYearRepository;
    }

    @Override
    public List<VehicleYear> getAll() {
        List<VehicleYear> years = vehicleYearRepository.findAll();
        List<VehicleYear> sorted = years.stream().sorted(Comparator.comparing(VehicleYear::getYear)).toList();

        return sorted;
    }

    @Override
    public VehicleYear getById(long id) {
        return vehicleYearRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Year not found"));
    }

    @Override
    public VehicleYear getByYear(int year) {
        return vehicleYearRepository.findByYear(year)
                .orElseGet(() -> {
            VehicleYear newYear = new VehicleYear();
            newYear.setYear(year);
            return create(newYear);
        });
    }

    @Override
    public VehicleYear create(VehicleYear vehicleYear) {
        VehicleYear existingYear = vehicleYearRepository.findByYear(vehicleYear.getYear())
                .orElse(null);

        if(existingYear != null){
            throw new EntityDuplicateException("Year already exists");
        }

        return vehicleYearRepository.saveAndFlush(vehicleYear);
    }
}
