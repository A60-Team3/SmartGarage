package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.repositories.RoleRepository;
import org.example.smartgarage.services.contracts.RoleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByAuthority(UserRole userRole) {
        return roleRepository.findByUserRole(userRole)
                .orElseThrow(() -> new EntityNotFoundException("Role", "authority", userRole.name()));
    }
}
