package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.models.ProfilePicture;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.repositories.contracts.PictureRepository;
import org.example.smartgarage.services.contracts.PictureService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;

    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @Override
    public List<ProfilePicture> getAll(){
        return pictureRepository.findAll();
    }

    @Override
    public ProfilePicture get(String url){
        return pictureRepository.findByPhotoUrl(url).orElse(null);
    }

    @Override
    public ProfilePicture savePhoto(String photoUrl, UserEntity user) {
        ProfilePicture profilePicture =
                user.getProfilePicture() != null
                        ? pictureRepository.findByPhotoUrl(user.getProfilePicture().getPhotoUrl()).get()
                        : null;

        if (profilePicture == null) {

            profilePicture = new ProfilePicture(photoUrl);
//            profilePicture.setUserId(user);
//
//            pictureRepository.savePhoto(profilePicture);
        } else {
            //TODO when updating must delete existing in cloudinary ??
            profilePicture.setPhotoUrl(photoUrl);
//            pictureRepository.updatePhoto(profilePicture);
        }

        return profilePicture;
    }

    @Override
    public void removePhoto(UserEntity user) {
        ProfilePicture profilePicture = pictureRepository.findByPhotoUrl(user.getProfilePicture().getPhotoUrl())
                .orElse(null);

        if (profilePicture != null) {
//            pictureRepository.deletePhoto(profilePicture);
        } else {
            throw new EntityNotFoundException(
                    "Picture",
                    "user",
                    user.getFirstName() + " " + user.getLastName());
        }
    }
}
