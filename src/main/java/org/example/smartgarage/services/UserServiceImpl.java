package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.exceptions.IllegalFileUploadException;
import org.example.smartgarage.models.ProfilePicture;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.repositories.contracts.UserRepository;
import org.example.smartgarage.services.contracts.CloudinaryService;
import org.example.smartgarage.services.contracts.PictureService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.utils.ImageUploadHelper;
import org.example.smartgarage.utils.filtering.UserEntitySpecification;
import org.example.smartgarage.utils.filtering.UserFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final PictureService pictureService;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, CloudinaryService cloudinaryService, PictureService pictureService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.cloudinaryService = cloudinaryService;
        this.pictureService = pictureService;
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
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Page<UserEntity> findAll(int offset, int pageSize, UserFilterOptions userFilterOptions) {
        UserEntitySpecification specification = new UserEntitySpecification(userFilterOptions);

        return userRepository.findAll(specification, PageRequest.of(offset, pageSize));
    }

    @Override
    public UserEntity update(long userId, UserEntity updatedUserInfo, MultipartFile multipartFile) {
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

        if (multipartFile != null && !multipartFile.isEmpty()){
            ImageUploadHelper.assertAllowed(multipartFile, ImageUploadHelper.IMAGE_PATTERN);

            try {
                String photoUrl = cloudinaryService.uploadImage(multipartFile);
                ProfilePicture profilePicture = pictureService.savePhoto(photoUrl, user);
                user.setProfilePicture(profilePicture);
            } catch (IOException e) {
                throw new IllegalFileUploadException(e.getMessage());
            }
        }

        user.setFirstName(updatedUserInfo.getFirstName());
        user.setLastName(updatedUserInfo.getLastName());
        user.setUsername(updatedUserInfo.getUsername());
        if (updatedUserInfo.getPassword() != null){
            user.setPassword(passwordEncoder.encode(updatedUserInfo.getPassword()));
        }
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

    @Override
    public UserEntity findByPhoneNumber(String phoneNumber) {
        return userRepository.findByPhoneNumber(phoneNumber).orElse(null);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
