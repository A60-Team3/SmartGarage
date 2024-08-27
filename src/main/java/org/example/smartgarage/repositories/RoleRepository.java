package org.example.smartgarage.repositories;

import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByUserRole(UserRole userRole);
}
