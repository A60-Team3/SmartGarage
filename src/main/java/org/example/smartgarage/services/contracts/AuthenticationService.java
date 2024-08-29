package org.example.smartgarage.services.contracts;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.models.UserEntity;

public interface AuthenticationService {

    TokenDto jwtLogin(LoginDTO loginDTO);

    UserEntity registerCustomer(UserEntity customerRegistrationDto, HttpServletRequest request);

    UserEntity registerEmployee(UserEntity employeeRegistrationDto, HttpServletRequest request);
}
