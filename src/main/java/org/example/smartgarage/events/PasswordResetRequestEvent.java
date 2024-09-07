package org.example.smartgarage.events;

import org.example.smartgarage.models.UserEntity;

public record PasswordResetRequestEvent(UserEntity user, String token, String url) {
}
