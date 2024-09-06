package org.example.smartgarage.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeMvcController {

    @GetMapping
    public String getHomePage(){
        return "redirect:/garage/index";
    }

    @GetMapping("/garage")
    public String redirectHomePage(){
        return "redirect:/garage/index";
    }

    @GetMapping("/garage/")
    public String redirectAlsoToHomePage(){
        return "redirect:/garage/index";
    }
}
