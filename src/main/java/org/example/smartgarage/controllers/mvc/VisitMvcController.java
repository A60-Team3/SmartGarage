package org.example.smartgarage.controllers.mvc;

import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/garage/visits")
public class VisitMvcController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    public VisitMvcController(VisitService visitService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
    }

    @PreAuthorize("hasRole('MECHANIC')")
    @GetMapping("/mechanic")
    public String getVisitsByMechanic(@ModelAttribute("vehicleDto") VehicleInDTO dto,
                                      RedirectAttributes redirect,
                                      Model model){


//        List<Visit> visits = visitService.findAll(visitFilterOptions);
//        List<VisitOutDto> dtos = visits.stream().map(visitMapper::toDto).toList();


        return "redirect:/garage/mechanic";
    }
}
