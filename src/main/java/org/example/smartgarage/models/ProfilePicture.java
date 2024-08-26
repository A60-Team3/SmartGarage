package org.example.smartgarage.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.example.smartgarage.models.baseEntity.BaseEntity;

@Entity
@Table(name = "profile_pictures")
public class ProfilePicture extends BaseEntity {
    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    public ProfilePicture() {
    }

    public ProfilePicture(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
