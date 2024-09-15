package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.utils.filtering.UserFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<UserEntity> findByUsername(String username);

    UserEntity saveUser(UserEntity user);


    UserEntity getById(long id);

    List<UserEntity> findAll();

    Page<UserEntity> findAll(int offset, int pageSize, UserFilterOptions userFilterOptions);

    UserEntity update(long userId, UserEntity updatedUserInfo, MultipartFile multipartFile) throws IOException;

    void deleteUser(long userId);

    UserEntity findByPhoneNumber(String phoneNumber);

    UserEntity findByEmail(String email);
}
