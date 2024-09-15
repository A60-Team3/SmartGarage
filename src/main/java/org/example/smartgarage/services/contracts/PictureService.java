package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.ProfilePicture;
import org.example.smartgarage.models.UserEntity;

import java.util.List;

public interface PictureService {
    List<ProfilePicture> getAll();

    ProfilePicture get(String url);

    ProfilePicture savePhoto(String photoUrl, UserEntity user);

    void removePhoto(UserEntity user);
}
