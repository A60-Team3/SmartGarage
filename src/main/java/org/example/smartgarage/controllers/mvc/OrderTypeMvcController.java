package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.OrderTypeInDTO;
import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.OrderTypeMapper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.utils.filtering.OrderTypeFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/garage/services")
public class OrderTypeMvcController {
    private final OrderTypeService orderTypeService;
    private final OrderTypeMapper orderTypeMapper;

    public OrderTypeMvcController(OrderTypeService orderTypeService, OrderTypeMapper orderTypeMapper) {
        this.orderTypeService = orderTypeService;
        this.orderTypeMapper = orderTypeMapper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping
    public String getOrderTypes(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                @ModelAttribute("orderTypeFilterOptions")OrderTypeFilterOptions filterOptions,
                                Model model){

        filterOptions.removeInvalid();

        Page<ServiceType> orderTypes = orderTypeService.getAll(pageIndex, pageSize, filterOptions);

        model.addAttribute("orderTypes", orderTypes);
        model.addAttribute("currentPage", orderTypes.getNumber() + 1);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", orderTypes.getTotalPages());

        return "services";
    }

    @GetMapping("/new")
    public String showNewServiceTypePage(Model model){

        OrderTypeInDTO dto = new OrderTypeInDTO(null, null);
        model.addAttribute("orderType", dto);
        return "service-type-create";
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping("/new")
    public String createServiceType(@Valid @ModelAttribute("orderType") OrderTypeInDTO dto,
                                    BindingResult bindingResult,
                                    Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("orderType", dto);
            return "service-type-create";
        }

        try {
            ServiceType orderType = orderTypeMapper.toEntity(dto);
            orderTypeService.create(orderType);
            return "redirect:/garage/services";
        } catch (EntityDuplicateException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/{serviceTypeId}/update")
    public String showUpdateServiceTypePage(@PathVariable long serviceTypeId, Model model){

        ServiceType serviceType = null;
        try {
            serviceType = orderTypeService.getById(serviceTypeId);
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
        OrderTypeInDTO dto = orderTypeMapper.orderTypeToOrderTypeDTO(serviceType);

        model.addAttribute("orderType", dto);
        return "service-type-create";
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping("/{serviceTypeId}/update")
    public String updateServiceType(@PathVariable long serviceTypeId,
                                    @Valid @ModelAttribute("orderType") OrderTypeInDTO dto,
                                    BindingResult bindingResult,
                                    Model model){

        if(bindingResult.hasErrors()){
            model.addAttribute("orderType", dto);
            return "service-type-create";
        }

        try {
            ServiceType orderType = orderTypeMapper.toEntity(dto);
            orderTypeService.update(serviceTypeId, orderType);
            return "redirect:/garage/services";
        } catch (EntityDuplicateException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        } catch (EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.NOT_FOUND.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
}
