package org.example.smartgarage.models;


import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import org.example.smartgarage.models.baseEntity.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
public class VerificationToken extends BaseEntity {

    private final static int EXPIRATION_TIME_MINUTES = 60;

    private String token;

    @CreationTimestamp
    private LocalDateTime created;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, updatable = false)
    private UserEntity user;

    public VerificationToken() {
    }

    public VerificationToken(String token, UserEntity user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public boolean isExpired(){
        return LocalDateTime.now().isAfter(created.plusMinutes(EXPIRATION_TIME_MINUTES));
    }
}
