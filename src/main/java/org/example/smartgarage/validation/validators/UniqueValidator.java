package org.example.smartgarage.validation.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.repositories.contracts.UserRepository;
import org.example.smartgarage.validation.Unique;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

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

        Optional<UserEntity> user;

        switch (uniqueType) {
            case EMAIL -> user = userRepository.findByEmail(value);
            case PHONE -> user = userRepository.findByPhoneNumber(value);
            case USERNAME -> user = userRepository.findByUsername(value);
            default -> user = Optional.empty();
        }


        return user.map(this::validateOwner).orElse(true);
    }

    private boolean validateOwner(UserEntity user) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return user.getUsername().equals(username);
    }
}
