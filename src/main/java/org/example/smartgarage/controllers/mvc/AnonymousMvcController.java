package org.example.smartgarage.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/garage")
public class AnonymousMvcController {

    @GetMapping("/home")
    public String getHomePage(){
        return "index";
    }

    @GetMapping("/about")
    public String getGarageInfo(){
        return "about";
    }

    @GetMapping("/booking")
    public String getBooking(){
        return "booking";
    }

    @GetMapping("/contacts")
    public String getContacts(){
        return "contact";
    }

    @GetMapping("/services")
    public String getServices(){
        return "services";
    }

    @GetMapping("/team")
    public String getEmployees(){
        return "team";
    }

    @GetMapping("/reviews")
    public String getReviews(){
        return "reviews";
    }
}
