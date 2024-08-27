package org.example.smartgarage.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.events.CustomerRegistrationEvent;
import org.example.smartgarage.exceptions.AuthenticationException;
import org.example.smartgarage.exceptions.CustomAuthenticationException;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.security.jwt.JwtProvider;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.example.smartgarage.services.contracts.RoleService;
import org.example.smartgarage.services.contracts.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ApplicationEventPublisher eventPublisher;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, UserService userService, RoleService roleService,
                                     JwtProvider jwtProvider, AuthenticationManager authenticationManager, ApplicationEventPublisher eventPublisher) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public TokenDto jwtLogin(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(), loginDTO.password())
        );

        UserEntity user = userService.findByUsername(loginDTO.username())
                .filter(userEntity -> passwordEncoder.matches(loginDTO.password(), userEntity.getPassword()))
                .orElseThrow(() -> new AuthenticationException("No such user"));

        String jwtToken = jwtProvider.generateToken(new CustomUserDetails(user));

        return new TokenDto(jwtToken);
    }

    @Override
    public UserOutDto registerCustomer(CustomerRegistrationDto customerRegistrationDto, HttpServletRequest request) {
        String rndCustomerPassword = UUID.randomUUID().toString();

        UserEntity user = new UserEntity();
        user.setUsername(customerRegistrationDto.email());
        user.setPassword(passwordEncoder.encode(rndCustomerPassword));
        user.setEmail(customerRegistrationDto.email());
        user.setPhoneNumber(customerRegistrationDto.phoneNumber());
        user.setFirstName(customerRegistrationDto.firstName());
        user.setLastName(customerRegistrationDto.lastName());

        Role userRole = roleService.findByAuthority(UserRole.CUSTOMER);

        user.setRoles(Set.of((userRole)));

        UserEntity savedCustomer = userService.saveUser(user);
        CustomerRegistrationEvent customerRegistrationEvent = new CustomerRegistrationEvent(user, rndCustomerPassword, applicationUrl(request));

        eventPublisher.publishEvent(customerRegistrationEvent);

        return mapToUserOutDto(savedCustomer);
    }

    @Override
    public UserOutDto registerEmployee(EmployeeRegistrationDto employeeRegistrationDto, HttpServletRequest request) {
        //TODO implement email validation email for employees (ValidationToken, tokenDeleteScheduler)

        if (!employeeRegistrationDto.password().equals(employeeRegistrationDto.passwordConfirm())) {
            throw new CustomAuthenticationException("Password confirmation should match password.");
        }

        UserEntity user = new UserEntity();
        user.setUsername(employeeRegistrationDto.username());
        user.setPassword(passwordEncoder.encode(employeeRegistrationDto.password()));
        user.setEmail(employeeRegistrationDto.email());
        user.setPhoneNumber(employeeRegistrationDto.phoneNumber());
        user.setFirstName(employeeRegistrationDto.firstName());
        user.setLastName(employeeRegistrationDto.lastName());

        Role userRole = roleService.findByAuthority(UserRole.CLERK);

        user.setRoles(Set.of((userRole)));

        UserEntity savedEmployee = userService.saveUser(user);

        return mapToUserOutDto(savedEmployee);
    }

    private UserOutDto mapToUserOutDto(UserEntity user) {
        return new UserOutDto(
                String.format("%s %s", user.getFirstName(), user.getLastName()),
                user.getEmail(),
                user.getRegistered().format(DateTimeFormatter.ofPattern("yyyy MM dd")),
                user.getUpdated().format(DateTimeFormatter.ofPattern("yyyy MM dd")),
                user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet())
        );
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath();
    }
}
