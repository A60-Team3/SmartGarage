package org.example.smartgarage.controllers.mvc;

import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/garage")
public class UserMvcController {

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/admin")
    public String getHrPage() {
        return "admin";
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/customer")
    public String getCustomerPage() {
        return "customer";
    }

    @PreAuthorize("hasRole('CLERK')")
    @GetMapping("/clerk")
    public String getClerkPage() {
        return "clerk";
    }

    @PreAuthorize("hasRole('HR')")
    @GetMapping("/mechanic")
    public ModelAndView getMechanicPage(Model model) {
        return new ModelAndView("mechanic");
    }
}
