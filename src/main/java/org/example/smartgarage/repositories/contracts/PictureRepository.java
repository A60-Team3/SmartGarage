package org.example.smartgarage.repositories.contracts;

import org.example.smartgarage.models.ProfilePicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PictureRepository extends JpaRepository<ProfilePicture, Long> {
    Optional<ProfilePicture> findByPhotoUrl(String url);
}
