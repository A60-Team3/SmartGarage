package org.example.smartgarage.controllers.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/garage")
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> getJwtToken(@RequestBody LoginDTO loginDto) {

        return ResponseEntity.ok(authenticationService.jwtLogin(loginDto));
    }

    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/users")
    public ResponseEntity<UserOutDto> registerNewCustomer(@Valid @RequestBody CustomerRegistrationDto customerRegistrationDto,
                                                          HttpServletRequest request){
        UserOutDto userOutDto = authenticationService.registerCustomer(customerRegistrationDto, request);

        return new ResponseEntity<>(userOutDto, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('HR')")
    @PostMapping("/clerks")
    public ResponseEntity<UserOutDto> registerNewEmployee(@Valid @RequestBody EmployeeRegistrationDto employeeRegistrationDto,
                                                          HttpServletRequest request){
        UserOutDto userOutDto = authenticationService.registerEmployee(employeeRegistrationDto, request);

        return new ResponseEntity<>(userOutDto, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public void logoutUser() {
    }
}
