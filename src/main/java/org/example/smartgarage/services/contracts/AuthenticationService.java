package org.example.smartgarage.services.contracts;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.dtos.response.UserOutDto;

public interface AuthenticationService {

    TokenDto jwtLogin(LoginDTO loginDTO);

    UserOutDto registerCustomer(CustomerRegistrationDto customerRegistrationDto, HttpServletRequest request);

    UserOutDto registerEmployee(EmployeeRegistrationDto employeeRegistrationDto, HttpServletRequest request);
}
