package org.example.smartgarage.services.contracts;

import org.example.smartgarage.dtos.request.LoginDTO;
import org.example.smartgarage.dtos.response.TokenDto;

public interface AuthenticationService {

    TokenDto jwtLogin(LoginDTO loginDTO);
}
