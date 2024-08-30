package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.repositories.UserRepository;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.utils.filtering.UserEntitySpecification;
import org.example.smartgarage.utils.filtering.UserFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserEntity saveUser(UserEntity user) {
        return userRepository.save(user);
    }

    @Override
    public UserEntity getById(long id) {
        return userRepository.findById(id).orElseThrow();
    }

    @Override
    public Page<UserEntity> getAll(int offset, int pageSize, UserFilterOptions userFilterOptions) {

        UserEntitySpecification specification = new UserEntitySpecification(userFilterOptions);

        Sort.Direction direction;
        if (userFilterOptions.getSortOrder().isPresent()) {
            direction = Sort.Direction.fromString(userFilterOptions.getSortOrder().get());
        } else {
            direction = Sort.Direction.ASC;
        }
        String sortBy;
        if (userFilterOptions.getSortBy().isPresent()) {
            sortBy = userFilterOptions.getSortBy().get();
        } else {
            sortBy = "username";
        }

        Pageable pageable = PageRequest.of(offset, pageSize, Sort.by(direction, sortBy));

        return userRepository.findAll(specification, pageable);
    }

    @Override
    public UserEntity update(long userId, UserEntity updatedUserInfo) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        userRepository.findByUsername(updatedUserInfo.getUsername())
                .ifPresent(user1 -> {
                    if (user1.getId() != userId) {
                        throw new EntityDuplicateException("User", "username", updatedUserInfo.getUsername());
                    }
                });

        userRepository.findByEmail(updatedUserInfo.getEmail())
                .ifPresent(user1 -> {
                    if (user1.getId() != userId) {
                        throw new EntityDuplicateException("User", "email", updatedUserInfo.getEmail());
                    }
                });

        userRepository.findByPhoneNumber(updatedUserInfo.getPhoneNumber())
                .ifPresent(user1 -> {
                    if (user1.getId() != userId) {
                        throw new EntityDuplicateException("User", "phone number", updatedUserInfo.getPhoneNumber());
                    }
                });

        user.setFirstName(updatedUserInfo.getFirstName());
        user.setLastName(updatedUserInfo.getLastName());
        user.setUsername(updatedUserInfo.getUsername());
        user.setPassword(passwordEncoder.encode(updatedUserInfo.getPassword()));
        user.setEmail(updatedUserInfo.getEmail());
        user.setPhoneNumber(updatedUserInfo.getPhoneNumber());

        return userRepository.saveAndFlush(user);
    }

    @Override
    public void deleteUser(long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User", userId));

        userRepository.delete(user);
    }
}
