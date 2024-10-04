package org.example.smartgarage.services;

import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.helpers.CreationHelper;
import org.example.smartgarage.models.ProfilePicture;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.repositories.contracts.PictureRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class PictureServiceTests {

    @Mock
    private PictureRepository pictureRepository;

    @InjectMocks
    private PictureServiceImpl pictureService;

    private ProfilePicture mockAvatar;

    @BeforeEach
    public void setUp() {
        mockAvatar = CreationHelper.createMockProfilePicture();
    }

    @Test
    public void getAll_Should_Return_AllProfilePictures() {
        Mockito.when(pictureRepository.findAll())
                .thenReturn(List.of(mockAvatar));

        List<ProfilePicture> avatars = pictureService.getAll();

        Mockito.verify(pictureRepository).findAll();
        Assertions.assertAll(
                () -> Assertions.assertNotNull(avatars),
                () -> Assertions.assertEquals(1, avatars.size()),
                () -> Assertions.assertEquals(mockAvatar, avatars.get(0))
        );
    }

    @Test
    public void get_Should_Return_ProfilePictures_IfExist() {
        Mockito.when(pictureRepository.findByPhotoUrl(Mockito.anyString()))
                .thenReturn(Optional.of(mockAvatar));

        ProfilePicture profilePicture = pictureService.get("test");

        Mockito.verify(pictureRepository).findByPhotoUrl(Mockito.anyString());
        Assertions.assertAll(
                () -> Assertions.assertNotNull(profilePicture),
                () -> Assertions.assertEquals(mockAvatar, profilePicture)
        );
    }

    @Test
    public void get_Should_Return_Null_IfNotExist() {
        Mockito.when(pictureRepository.findByPhotoUrl(Mockito.anyString()))
                .thenReturn(Optional.empty());

        ProfilePicture profilePicture = pictureService.get("test");

        Mockito.verify(pictureRepository).findByPhotoUrl(Mockito.anyString());
        Assertions.assertNull(profilePicture);
    }

    @Test
    public void savePhoto_Should_SaveAvatar_When_NotExisting(){
        UserEntity mockUser = CreationHelper.createMockUser();

        pictureService.savePhoto("test", mockUser);

        Mockito.verify(pictureRepository).saveAndFlush(Mockito.any(ProfilePicture.class));
    }

    @Test
    public void savePhoto_Should_UpdateAvatar_When_Existing(){
        UserEntity mockUser = CreationHelper.createMockUser();
        mockUser.setProfilePicture(mockAvatar);

        Mockito.when(pictureRepository.findByPhotoUrl(Mockito.anyString()))
                        .thenReturn(Optional.of(mockAvatar));

        ProfilePicture profilePicture = pictureService.savePhoto("test", mockUser);

        Mockito.verify(pictureRepository).saveAndFlush(Mockito.any(ProfilePicture.class));
        Assertions.assertNotNull(profilePicture);
        Assertions.assertEquals(profilePicture, mockUser.getProfilePicture());
    }

    @Test
    public void removePhoto_Should_RemoveAvatar_IfUserHasOne(){
        UserEntity mockUser = CreationHelper.createMockUser();
        mockUser.setProfilePicture(mockAvatar);

        Mockito.when(pictureRepository.findByPhotoUrl(Mockito.anyString()))
                .thenReturn(Optional.of(mockAvatar));

        pictureService.removePhoto(mockUser);
        Mockito.verify(pictureRepository).delete(Mockito.any(ProfilePicture.class));
    }

    @Test
    public void removePhoto_Should_When_UserHasNoAvatar(){
        UserEntity mockUser = CreationHelper.createMockUser();
        mockUser.setProfilePicture(mockAvatar);

        Mockito.when(pictureRepository.findByPhotoUrl(Mockito.anyString()))
                .thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class,
                () ->         pictureService.removePhoto(mockUser));

    }

}
