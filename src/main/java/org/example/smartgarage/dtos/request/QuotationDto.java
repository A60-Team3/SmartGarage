package org.example.smartgarage.dtos.request;

import jakarta.validation.constraints.*;
import org.example.smartgarage.models.enums.CurrencyCode;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QuotationDto {
    public static final String emailRegex = "^[a-zA-Z0-9]+([._-][0-9a-zA-Z]+)*@[a-zA-Z0-9]+([.-][0-9a-zA-Z]+)*\\.[a-zA-Z]{2,}$";

    @NotBlank(message = "Field is mandatory")
    private String firstName;

    @NotBlank(message = "Field is mandatory")
    private String lastName;

    @Email(regexp = emailRegex, message = "Email should be valid")
    private String email;

    @NotBlank(message = "Field is mandatory")
    private String carBrand;

    @NotBlank(message = "Field is mandatory")
    private String carModel;

    @Min(1886)
    private int carYear;

    @Future(message = "Must be in advance. For emergency visit contact us by phone")
    private LocalDate desiredDate;

    @NotEmpty(message = "Please select a service")
    private List<String> requiredServices;

    private CurrencyCode desiredCurrency = CurrencyCode.BGN;

    public QuotationDto() {
        this.requiredServices = new ArrayList<>();
    }

    public QuotationDto(String firstName, String lastName, String email,
                        String carBrand, String carModel, int carYear,
                        LocalDate desiredDate, List<String> requiredServices,
                        CurrencyCode desiredCurrency) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.carBrand = carBrand;
        this.carModel = carModel;
        this.carYear = carYear;
        this.desiredDate = desiredDate;
        this.requiredServices = requiredServices;
        this.desiredCurrency = desiredCurrency;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }


    public int getCarYear() {
        return carYear;
    }

    public void setCarYear(int carYear) {
        this.carYear = carYear;
    }

    public LocalDate getDesiredDate() {
        return desiredDate;
    }

    public void setDesiredDate(LocalDate desiredDate) {
        this.desiredDate = desiredDate;
    }

    public List<String> getRequiredServices() {
        return requiredServices;
    }

    public void setRequiredServices(List<String> requiredServices) {
        this.requiredServices = requiredServices;
    }

    public CurrencyCode getDesiredCurrency() {
        return desiredCurrency;
    }

    public void setDesiredCurrency(CurrencyCode desiredCurrency) {
        this.desiredCurrency = desiredCurrency;
    }
}
