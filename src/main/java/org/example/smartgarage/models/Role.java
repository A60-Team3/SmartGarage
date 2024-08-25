package org.example.smartgarage.models;

import jakarta.persistence.*;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;

import java.util.Objects;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    public Role(UserRole type) {
        this.userRole = type;
    }

    public Role() {
    }

    public void setAuthority(UserRole type) {
        this.userRole = type;
    }

    public UserRole getRole(){
        return userRole;
    }

    @Override
    public String getAuthority() {
        return userRole.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(userRole, role.userRole);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(userRole);
    }
}
