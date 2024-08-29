package org.example.smartgarage.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.events.CustomerRegistrationEvent;
import org.example.smartgarage.exceptions.AuthenticationException;
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
import java.util.HashSet;
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
    public UserEntity registerCustomer(UserEntity user, HttpServletRequest request) {
        String rndCustomerPassword = UUID.randomUUID().toString();

        user.setUsername(user.getEmail());
        user.setPassword(passwordEncoder.encode(rndCustomerPassword));

        Role userRole = roleService.findByAuthority(UserRole.CUSTOMER);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        user.setRoles(roles);

        UserEntity savedCustomer = userService.saveUser(user);

        eventPublisher.publishEvent(new CustomerRegistrationEvent(savedCustomer, rndCustomerPassword, applicationUrl(request)));

        return savedCustomer;
    }

    @Override
    public UserEntity registerEmployee(UserEntity employee, HttpServletRequest request) {
        //TODO implement email validation email for employees (ValidationToken, tokenDeleteScheduler)

        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Role userRole = roleService.findByAuthority(UserRole.CLERK);

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        employee.setRoles(roles);

        return userService.saveUser(employee);
    }

    private String applicationUrl(HttpServletRequest request) {
        return "http://"
                + request.getServerName()
                + ":"
                + request.getServerPort()
                + request.getContextPath();
    }
}
