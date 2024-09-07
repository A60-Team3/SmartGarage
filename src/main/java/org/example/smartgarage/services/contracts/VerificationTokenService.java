package org.example.smartgarage.services.contracts;

import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.VerificationToken;

public interface VerificationTokenService {
    void save(UserEntity user, String token);

    VerificationToken findByToken(String token);

    void removeToken(VerificationToken dbToken);
}
