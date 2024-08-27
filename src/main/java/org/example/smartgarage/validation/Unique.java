package org.example.smartgarage.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.validation.validators.UniqueValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = UniqueValidator.class)
public @interface Unique {
    String message() default "Field should be unique";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    UniqueType type();

}


