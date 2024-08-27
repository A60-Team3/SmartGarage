package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.UserEntity;

import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByUsername(String username);

    UserEntity saveUser(UserEntity user);
}
