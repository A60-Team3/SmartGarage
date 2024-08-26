package org.example.smartgarage.services;

import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.repositories.contracts.UserRepository;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.security.jwt.JwtProvider;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;

    public AuthenticationServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository, JwtProvider jwtProvider, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }


    @Override
    public TokenDto jwtLogin(LoginDTO loginDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.username(),loginDTO.password())
        );

        UserEntity user = userRepository.findByUsername(loginDTO.username())
                .filter(userEntity -> passwordEncoder.matches(loginDTO.password(), userEntity.getPassword()))
                .orElseThrow();

        String jwtToken = jwtProvider.generateToken(new CustomUserDetails(user));

        return new TokenDto(jwtToken);
    }
}
