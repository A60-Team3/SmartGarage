package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.repositories.contracts.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @InjectMocks
    private RoleServiceImpl roleService;
    @Mock
    private RoleRepository roleRepository;

    @Test
    public void findAll_Should_callRepository(){
        Role role = new Role(UserRole.HR);
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));

        Assertions.assertEquals(List.of(role), roleService.findAll());
        Mockito.verify(roleRepository, Mockito.times(1))
                .findAll();
    }

    @Test
    public void findByAuthority_Should_ReturnRole_When_InputIsValid(){
        Role role = new Role(UserRole.HR);
        Mockito.when(roleRepository.findByUserRole(Mockito.any(UserRole.class)))
                .thenReturn(Optional.of(role));

        Assertions.assertEquals(role, roleService.findByAuthority(UserRole.HR));
        Mockito.verify(roleRepository, Mockito.times(1))
                .findByUserRole(Mockito.any(UserRole.class));
    }

    @Test
    public void findByAuthority_Should_Throw_When_NoSuchRole(){
        Mockito.when(roleRepository.findByUserRole(Mockito.any(UserRole.class)))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> roleService.findByAuthority(UserRole.HR));
    }
}
