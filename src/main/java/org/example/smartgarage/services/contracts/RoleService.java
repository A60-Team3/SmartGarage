package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.enums.UserRole;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> findAll();

    Role findByAuthority(UserRole userRole);
}
