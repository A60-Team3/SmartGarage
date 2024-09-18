package org.example.smartgarage.services;

import org.example.smartgarage.helpers.CreationHelper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.models.VerificationToken;
import org.example.smartgarage.repositories.contracts.VerificationTokenRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class VerificationTokesServiceTests {
    @InjectMocks
    private VerificationTokenServiceImpl verificationTokenService;
    @Mock
    private VerificationTokenRepository tokenRepository;

    @Test
    public void save_Should_CallRepository(){
        verificationTokenService.save(CreationHelper.createMockUser(), "token");
        Mockito.verify(tokenRepository, Mockito.times(1))
                .saveAndFlush(Mockito.any(VerificationToken.class));
    }

    @Test
    public void findByToken_Should_ReturnToken_When_InputIsValid(){
        VerificationToken token = new VerificationToken("String", CreationHelper.createMockUser());

        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.of(token));

        VerificationToken result = verificationTokenService.findByToken("token");

        Assertions.assertEquals(result, token);
        Mockito.verify(tokenRepository, Mockito.times(1))
                .findByToken(Mockito.anyString());
    }

    @Test
    public void findByToken_Should_ReturnNull_When_TokenIsMissing(){
        Mockito.when(tokenRepository.findByToken(Mockito.anyString()))
                .thenReturn(Optional.empty());

        VerificationToken result = verificationTokenService.findByToken("token");

        Assertions.assertNull(result);
        Mockito.verify(tokenRepository, Mockito.times(1))
                .findByToken(Mockito.anyString());
    }

    @Test
    public void removeToken_Should_CallRepository(){
        VerificationToken token = new VerificationToken("string", CreationHelper.createMockUser());
        verificationTokenService.removeToken(token);
        Mockito.verify(tokenRepository, Mockito.times(1))
                .delete(Mockito.any(VerificationToken.class));
    }
}
