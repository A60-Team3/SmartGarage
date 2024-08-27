package org.example.smartgarage.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.repositories.UserRepository;
import org.example.smartgarage.validation.Unique;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final UserRepository userRepository;
    private UniqueType uniqueType;

    public UniqueValidator(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        };
    }
}
