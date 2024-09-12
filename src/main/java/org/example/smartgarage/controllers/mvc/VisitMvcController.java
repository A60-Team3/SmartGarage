package org.example.smartgarage.controllers.mvc;

import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.services.contracts.VisitService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/garage/visits")
public class VisitMvcController {

    private final VisitService visitService;
    private final VisitMapper visitMapper;

    public VisitMvcController(VisitService visitService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.visitMapper = visitMapper;
    }
}
