package org.example.smartgarage.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/garage")
public class AuthenticationMVCController {

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }
}
