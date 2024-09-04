package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.smartgarage.models.enums.UniqueType;
import org.example.smartgarage.validation.Unique;

public record EmployeeRegistrationDto(
        @NotBlank(message = "Username is mandatory")
        @Size(min = 2, max = 20)
        @Unique(type = UniqueType.USERNAME, message = "Username already exists")
        String username,

        @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
        String firstName,

        @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
        String lastName,

        @NotBlank(message = "Email is mandatory")
        @Email(regexp = emailRegex, message = "Email should be valid")
        @Unique(type = UniqueType.EMAIL, message = "Email already exists")
        String email,

        @NotBlank(message = "Phone number is mandatory")
        @Pattern(regexp = phoneRegex, message = "Phone number must consist of 10 digits")
        @Unique(type = UniqueType.PHONE, message = "Phone number already exists")
        String phoneNumber,

        @NotBlank(message = "Password is mandatory")
        @Pattern(regexp = passwordRegex, message = "Password should contain a capital letter, digit, and special symbol")
        @Size(min = 8, message = "Password must be at least 8 characters long")
        String password,

        @NotBlank(message = "This field is mandatory")
        String passwordConfirm

) {
    public static final String emailRegex = "^[a-zA-Z0-9]+([._-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+([.-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$";
    public static final String phoneRegex = "^\\d{10}$";
    public static final String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()+\\-_=])[A-Za-z\\d!@#$%^&*()+\\-_=]{8,}$";

}
