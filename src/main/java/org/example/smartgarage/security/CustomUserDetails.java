package org.example.smartgarage.security;

import org.example.smartgarage.models.UserEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class CustomUserDetails extends User {
    private final Long id;

    public CustomUserDetails(UserEntity user) {
        super(user.getUsername(), user.getPassword(),
                user.isActive(), true,
                true, user.isActive(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole().name()))
                        .collect(Collectors.toList())
        );

        this.id = user.getId();
    }

    public Long getId() {
        return id;
    }
}
