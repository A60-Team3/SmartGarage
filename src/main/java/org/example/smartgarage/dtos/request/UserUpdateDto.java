package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record UserUpdateDto(
        @NotBlank
        @Size(min = 2, max = 32, message = "First name must be between 2 and 32 characters")
        String firstName,

        @NotBlank
        @Size(min = 2, max = 32, message = "Last name must be between 2 and 32 characters")
        String lastName,

        @NotBlank
        @Size(min = 2, max = 20)
        String username,

        @NotBlank
        @Size(min = 8, message = "Password must be at least 8 characters long")
        @Pattern(regexp = passwordRegex, message = "Password should contain a capital letter, digit, and special symbol")
        String password,

        @NotBlank
        @Email(regexp = emailRegex, message = "Email should be valid")
        String email,

        @NotBlank
        @Pattern(regexp = phoneRegex, message = "Phone number should be valid")
        String phoneNumber,

        MultipartFile profilePic
) {

    public static final String emailRegex = "^[a-zA-Z0-9]+([._-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+([.-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$";
    public static final String phoneRegex = "^\\d{10}$";
    public static final String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()+\\-_=])[A-Za-z\\d!@#$%^&*()+\\-_=]{8,}$";

}
