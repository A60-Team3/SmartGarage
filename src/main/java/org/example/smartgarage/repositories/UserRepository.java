package org.example.smartgarage.repositories;

import org.example.smartgarage.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhoneNumber(String phoneNumber);

    UserEntity findAllByEmailContainsIgnoreCaseAndUsernameContainsIgnoreCase(String email, String username);
}
