package org.example.smartgarage.services;

import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.VerificationToken;
import org.example.smartgarage.repositories.contracts.VerificationTokenRepository;
import org.example.smartgarage.services.contracts.VerificationTokenService;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {
    private final VerificationTokenRepository tokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void save(UserEntity user, String token) {
        tokenRepository.saveAndFlush(new VerificationToken(token, user));
    }

    @Override
    public VerificationToken findByToken(String token) {
        return tokenRepository.findByToken(token).orElse(null);
    }

    @Override
    public void removeToken(VerificationToken dbToken) {
        tokenRepository.delete(dbToken);
    }
}
