package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/garage/visits")
public class VisitMvcController {

    private final VisitService visitService;
    private final OrderTypeService orderTypeService;
    private final VisitMapper visitMapper;

    public VisitMvcController(VisitService visitService, OrderTypeService orderTypeService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.orderTypeService = orderTypeService;
        this.visitMapper = visitMapper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @ModelAttribute("availableServices")
    public List<ServiceType> getAvailableServices() {
        return orderTypeService.getAll();
    }

    @GetMapping
    public String getVisits(@RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                            @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                            @ModelAttribute("visitFilterOptions") VisitFilterOptions filterOptions,
                            @RequestParam(value = "rescheduled", required = false) boolean rescheduled,
                            @RequestParam(value = "rescheduleVisitId", required = false) Integer rescheduleVisitId,
                            Model model) {

        filterOptions.removeInvalid();

        if (rescheduled) {
            pageIndex = visitService.calculateVisitPage(rescheduleVisitId, filterOptions, pageSize);
        }
        Map<String, Long> vehicleOwners = new HashMap<>();

        Page<Visit> visits = visitService.findAll(filterOptions, pageIndex - 1, pageSize);
        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        visits.stream().forEach(visit -> {
            vehicleOwners.put(visit.getVehicle().getLicensePlate(),visit.getClient().getId());
        });

        model.addAttribute("visits", visitOutDtos);
        model.addAttribute("customerMap",vehicleOwners);
        model.addAttribute("totalPages",visits.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", visits.getNumber() + 1);

        return "visits";
    }
}
