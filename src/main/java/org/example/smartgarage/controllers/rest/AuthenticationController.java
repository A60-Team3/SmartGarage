package org.example.smartgarage.controllers.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.mappers.UserMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/garage")
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;


    public AuthenticationController(AuthenticationService authenticationService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> getJwtToken(@RequestBody LoginDTO loginDto) {

        return ResponseEntity.ok(authenticationService.jwtLogin(loginDto));
    }

    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/users")
    public ResponseEntity<UserOutDto> registerNewCustomer(@Valid @RequestBody CustomerRegistrationDto customerRegistrationDto,
                                                          HttpServletRequest request) {
        UserEntity customer = userMapper.toEntity(customerRegistrationDto);
        UserEntity savedCustomer = authenticationService.registerCustomer(customer, request);
        UserOutDto userOutDto = userMapper.toDto(savedCustomer);

        return new ResponseEntity<>(userOutDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/clerks")
    public ResponseEntity<UserOutDto> registerNewEmployee(@Valid @RequestBody EmployeeRegistrationDto employeeRegistrationDto,
                                                 HttpServletRequest request) {

        if (!employeeRegistrationDto.password().equals(employeeRegistrationDto.passwordConfirm())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password confirmation should match password.");
        }
        UserEntity employee = userMapper.toEntity(employeeRegistrationDto);
        UserEntity savedEmployee = authenticationService.registerEmployee(employee, request);
        UserOutDto userOutDto = userMapper.toDto(savedEmployee);

        return new ResponseEntity<>(userOutDto, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public void logoutUser() {
    }
}
