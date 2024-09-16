package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.request.VisitInDto;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.mappers.VisitMapper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VehicleService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/garage/visits")
public class VisitMvcController {

    private final VisitService visitService;
    private final OrderTypeService orderTypeService;
    private final UserService userService;
    private final VehicleService vehicleService;
    private final VisitMapper visitMapper;

    public VisitMvcController(VisitService visitService, OrderTypeService orderTypeService, UserService userService, VehicleService vehicleService, VisitMapper visitMapper) {
        this.visitService = visitService;
        this.orderTypeService = orderTypeService;
        this.userService = userService;
        this.vehicleService = vehicleService;
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

        Page<Visit> visits = visitService.findAll(filterOptions, pageIndex - 1, pageSize);
        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        Map<String, Long> vehicleOwners = new HashMap<>();
        visits.stream().forEach(visit -> {
            vehicleOwners.put(visit.getVehicle().getLicensePlate(), visit.getClient().getId());
        });

        model.addAttribute("visits", visitOutDtos);
        model.addAttribute("customerMap", vehicleOwners);
        model.addAttribute("totalPages", visits.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", visits.getNumber() + 1);

        return "visits";
    }

    @GetMapping("/client/{customerId}")
    public String getVisitsForCustomer(@PathVariable long customerId,
                                       @RequestParam(value = "pageIndex", defaultValue = "1") int pageIndex,
                                       @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                       @ModelAttribute("visitFilterOptions") VisitFilterOptions filterOptions,
                                       @AuthenticationPrincipal CustomUserDetails principal,
                                       Model model) {

        filterOptions.removeInvalid();
        if (principal.getId() != customerId) {
            return "403";
        }
        Page<Visit> visits = visitService.findAll(filterOptions, pageIndex - 1, pageSize);

        List<VisitOutDto> visitOutDtos = visits.stream().map(visitMapper::toDto).toList();

        model.addAttribute("visits", visitOutDtos);
        model.addAttribute("totalPages", visits.getTotalPages());
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("currentPage", visits.getNumber() + 1);

        return "visits-client";

    }

    @GetMapping("/new")
    public String getCreateVisitPage(@ModelAttribute("visitInDto") VisitInDto visitInDto,
                                     Model model) {
        List<UserEntity> allUsers = userService.findAll();
        List<Vehicle> allVehicles = vehicleService.getAll();

        model.addAttribute("users", allUsers);
        model.addAttribute("vehicles", allVehicles);

        return "visit-create";
    }

    @PostMapping("/visits/new/date")
    public String setBookedDate(@ModelAttribute("visitInDto") VisitInDto visitInDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        if (bindingResult.hasErrors()) {
            return "visit-create";
        }

        VisitFilterOptions visitFilterOptions =
                new VisitFilterOptions(null, null, null, null, null, null,
                        TimeOperator.EQUAL, visitInDto.bookedDate(), null, null,
                        null, null, null);

        List<Visit> visitsForDate = visitService.findAll(visitFilterOptions);
        if (visitsForDate.size() >= 6) {
            bindingResult.rejectValue("bookedDate", "This date is fully booked");
            return "visit-create";
        }

        redirectAttributes.addFlashAttribute(visitInDto);

        return "visit-create";
    }


}
