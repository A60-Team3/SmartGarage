package org.example.smartgarage.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.repositories.contracts.UserRepository;
import org.example.smartgarage.repositories.contracts.VehicleRepository;
import org.example.smartgarage.validation.Unique;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;
    private UniqueType uniqueType;

    public UniqueValidator(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public void initialize(Unique unique) {
        this.uniqueType = unique.type();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        return switch (uniqueType) {
            case EMAIL -> userRepository.findByEmail(value).isEmpty();
            case PHONE -> userRepository.findByPhoneNumber(value).isEmpty();
            case USERNAME -> userRepository.findByUsername(value).isEmpty();
            case VIN,REGISTRY -> vehicleRepository.findVehicleByLicensePlateOrVin(value, value) != null;
        };
    }
}
