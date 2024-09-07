package org.example.smartgarage.services.contracts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.models.UserEntity;

public interface AuthenticationService {

    TokenDto jwtLogin(LoginDTO loginDTO);

    UserEntity registerCustomer(UserEntity customer, HttpServletRequest request);

    UserEntity registerEmployee(UserEntity employee, HttpServletRequest request);

    void sendResetEmail(UserEntity existingUser, HttpServletRequest request);

    void resetPassword(String password, long userId, HttpServletRequest request);
}
