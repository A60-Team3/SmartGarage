package org.example.smartgarage.events;

import org.example.smartgarage.models.UserEntity;

public record CustomerRegistrationEvent(UserEntity user,
                                        String password,
                                        String appUrl) {
}
