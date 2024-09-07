package org.example.smartgarage.events;

import org.example.smartgarage.models.UserEntity;

public record PasswordResetCompleteEvent(UserEntity user, String url) {
}
