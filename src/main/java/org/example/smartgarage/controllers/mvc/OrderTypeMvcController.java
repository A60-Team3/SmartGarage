package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.mappers.OrderTypeMapper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.utils.filtering.OrderTypeFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("totalPages", orderTypes.getTotalPages());

        return "services";
    }
}
