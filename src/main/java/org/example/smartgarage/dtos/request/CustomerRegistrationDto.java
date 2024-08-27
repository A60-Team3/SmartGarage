package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.validation.Unique;

public record CustomerRegistrationDto(
        @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
        String firstName,

        @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(regexp = emailRegex, message = "Email should be valid")
        @Unique(type = UniqueType.EMAIL,message = "Email already exists")
        String email,

        @NotBlank(message = "Phone number is mandatory")
        @Unique(type = UniqueType.PHONE, message = "Phone number already exists")
        @Pattern(regexp = phoneRegex, message = "Phone number must consist of 10 digits")
        @Size(min = 10, max = 10)
        String phoneNumber

) {
    public static final String emailRegex = "^[a-zA-Z0-9]+([._-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+([.-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$";
    public static final String phoneRegex = "^\\d{10}$";
}
