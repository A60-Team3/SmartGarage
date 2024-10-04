package org.example.smartgarage.services;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.events.CustomerRegistrationEvent;
import org.example.smartgarage.events.PasswordResetCompleteEvent;
import org.example.smartgarage.events.PasswordResetRequestEvent;
import org.example.smartgarage.exceptions.AuthenticationException;
import org.example.smartgarage.helpers.CreationHelper;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.security.jwt.JwtProvider;
import org.example.smartgarage.services.contracts.RoleService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VerificationTokenService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTests {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

    @Mock
    private VerificationTokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    public void jwtLogin_Should_Return_ValidToken() {
        UserEntity user = CreationHelper.createMockUser();
        LoginDTO loginDTO = new LoginDTO(user.getUsername(), user.getPassword());
        String mockJwtToken = "mockToken";

        Mockito.when(authenticationManager.authenticate(Mockito.any(Authentication.class)))
                .thenReturn(new TestingAuthenticationToken(user, user.getPassword()));
        Mockito.when(userService.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);
        Mockito.when(jwtProvider.generateToken(Mockito.any(UserDetails.class)))
                .thenReturn(mockJwtToken);

        TokenDto result = authenticationService.jwtLogin(loginDTO);

        Assertions.assertEquals(result.jwtToken(), mockJwtToken);
    }

    @Test
    public void jwtLogin_Should_Throw_When_UserIsInvalid() {
        UserEntity user = CreationHelper.createMockUser();
        LoginDTO loginDTO = new LoginDTO(user.getUsername(), user.getPassword());

        Mockito.when(userService.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authenticationService.jwtLogin(loginDTO)
        );

        Mockito.verify(jwtProvider, Mockito.never())
                .generateToken(Mockito.any(UserDetails.class));
        Mockito.verify(passwordEncoder, Mockito.never())
                .matches(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    public void jwtLogin_Should_Throw_When_PasswordIsInvalid() {
        UserEntity user = CreationHelper.createMockUser();
        LoginDTO loginDTO = new LoginDTO(user.getUsername(), user.getPassword());

        Mockito.when(userService.findByUsername(Mockito.anyString()))
                .thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(false);

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authenticationService.jwtLogin(loginDTO)
        );

        Mockito.verify(jwtProvider, Mockito.never())
                .generateToken(Mockito.any(UserDetails.class));
    }

    @Test
    public void registerCustomer_Should_Return_RegisteredCustomerManyRoles() {
        UserEntity mockCustomer = CreationHelper.createMockUser();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockCustomer.getPassword());
        Mockito.when(userService.saveUser(Mockito.any(UserEntity.class)))
                .thenReturn(mockCustomer);

        UserEntity user = authenticationService.registerCustomer(mockCustomer, request);

        Assertions.assertEquals(mockCustomer, user);

        Mockito.verify(eventPublisher, Mockito.times(1))
                .publishEvent(Mockito.any(CustomerRegistrationEvent.class));
        Mockito.verify(userService, Mockito.times(1))
                .saveUser(Mockito.any(UserEntity.class));
    }

    @Test
    public void registerCustomer_Should_Return_RegisteredCustomerSingleRole() {
        UserEntity mockCustomer = CreationHelper.createMockCustomer();
        mockCustomer.setRoles(null);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockCustomer.getPassword());
        Mockito.when(roleService.findByAuthority(Mockito.any(UserRole.class)))
                .thenReturn(new Role(UserRole.CUSTOMER));
        Mockito.when(userService.saveUser(Mockito.any(UserEntity.class)))
                .thenReturn(mockCustomer);

        UserEntity user = authenticationService.registerCustomer(mockCustomer, request);

        Assertions.assertEquals(mockCustomer, user);

        Mockito.verify(eventPublisher, Mockito.times(1))
                .publishEvent(Mockito.any(CustomerRegistrationEvent.class));
        Mockito.verify(userService, Mockito.times(1))
                .saveUser(Mockito.any(UserEntity.class));
    }

    @Test
    public void registerEmployee_Should_Return_RegisteredHR_When_NoOtherEmployees() {
        UserEntity mockEmployee = CreationHelper.createMockUser();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Role role = new Role(UserRole.HR);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockEmployee.getPassword());
        Mockito.when(userService.findAll())
                .thenReturn(Collections.emptyList());
        Mockito.when(roleService.findByAuthority(Mockito.any(UserRole.class)))
                .thenReturn(role);
        Mockito.when(userService.saveUser(Mockito.any(UserEntity.class)))
                .thenReturn(mockEmployee);

        UserEntity user = authenticationService.registerEmployee(mockEmployee, request);

        Assertions.assertTrue(mockEmployee.getRoles().contains(role));
        Assertions.assertNotNull(user);
        Mockito.verify(userService, Mockito.times(1))
                .saveUser(Mockito.any(UserEntity.class));
    }

    @Test
    public void registerEmployee_Should_Return_RegisteredClerk_When_EmployeesExist() {
        UserEntity mockEmployee = CreationHelper.createMockUser();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Role role = new Role(UserRole.CLERK);

        Mockito.when(passwordEncoder.encode(Mockito.anyString()))
                .thenReturn(mockEmployee.getPassword());
        Mockito.when(userService.findAll())
                .thenReturn(List.of(mockEmployee));
        Mockito.when(roleService.findByAuthority(Mockito.any(UserRole.class)))
                .thenReturn(role);
        Mockito.when(userService.saveUser(Mockito.any(UserEntity.class)))
                .thenReturn(mockEmployee);

        UserEntity user = authenticationService.registerEmployee(mockEmployee, request);

        Assertions.assertTrue(mockEmployee.getRoles().contains(role));
        Mockito.verify(userService, Mockito.times(1))
                .saveUser(Mockito.any(UserEntity.class));
    }

    @Test
    public void sendResetEmail_Should_SendEmail() {
        UserEntity mockEmployee = CreationHelper.createMockUser();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        String testToken = "Token";

        Mockito.doNothing()
                .when(tokenService)
                .save(Mockito.any(UserEntity.class), Mockito.anyString());

        authenticationService.sendResetEmail(mockEmployee, request);
        Mockito.verify(eventPublisher, Mockito.times(1))
                .publishEvent(Mockito.any(PasswordResetRequestEvent.class));
    }

    @Test
    public void resetPassword_Should_CreateToken() throws IOException {
        UserEntity mockEmployee = CreationHelper.createMockUser();
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);

        Mockito.when(userService.getById(Mockito.anyLong()))
                .thenReturn(mockEmployee);
        Mockito.when(userService.update(mockEmployee.getId(), mockEmployee, null))
                .thenReturn(mockEmployee);

        authenticationService.resetPassword(mockEmployee.getPassword(), mockEmployee.getId(), request);

        Mockito.verify(userService, Mockito.times(1)).getById(Mockito.anyLong());
        Mockito.verify(userService, Mockito.times(1))
                .update(Mockito.anyLong(), Mockito.any(UserEntity.class), Mockito.any());
        Mockito.verify(eventPublisher, Mockito.times(1))
                .publishEvent(Mockito.any(PasswordResetCompleteEvent.class));
    }
}
