package org.example.smartgarage.dtos.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PasswordResetDto {

    public static final String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()+\\-_=])[A-Za-z\\d!@#$%^&*()+\\-_=]{8,}$";

    @NotBlank
    @Pattern(regexp = passwordRegex, message = "Password should contain a capital letter, digit, and special symbol")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;

    @NotBlank(message = "This field is mandatory")
    private String passwordConfirm;

    public String getPassword() {
        return password;
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }
}

