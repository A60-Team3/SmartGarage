package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.OrderInDTO;
import org.example.smartgarage.dtos.request.OrderListInDTO;
import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.exceptions.UserMismatchException;
import org.example.smartgarage.exceptions.VisitMismatchException;
import org.example.smartgarage.mappers.OrderMapper;
import org.example.smartgarage.models.Order;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.services.contracts.OrderService;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VisitService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/garage")
public class OrderMvcController {
    private final OrderService orderService;
    private final OrderTypeService orderTypeService;
    private final UserService userService;
    private final VisitService visitService;
    private final OrderMapper orderMapper;

    public OrderMvcController(OrderService orderService, OrderTypeService orderTypeService, UserService userService, VisitService visitService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderTypeService = orderTypeService;
        this.userService = userService;
        this.visitService = visitService;
        this.orderMapper = orderMapper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @PreAuthorize("hasRole('CLERK')")
    @GetMapping("/visits/{visitId}/orders/new")
    public String showNewOrdersPage(@PathVariable long visitId, Model model){


        Visit visit = visitService.findById(visitId);
        List<ServiceType> list = visit.getServices().stream().map(Order::getServiceType).toList();
        List<ServiceType> orderTypes = orderTypeService.getAll().stream()
                .filter(order -> !list.contains(order)).toList();

        model.addAttribute("visit", visit);
        model.addAttribute("orderTypes", orderTypes);
        model.addAttribute("orders", new OrderListInDTO(null));
        return "orders-create";
    }

    @PreAuthorize("hasRole('CLERK')")
    @PostMapping("/visits/{visitId}/orders/new")
    public String createOrders(@PathVariable long visitId,
                               @Valid @ModelAttribute("orders") OrderListInDTO orders,
                               BindingResult bindingResult,
                               Model model){

        Visit visit = visitService.findById(visitId);
        if(bindingResult.hasErrors()){
            List<ServiceType> orderTypes = orderTypeService.getAll();
            model.addAttribute("visit", visit);
            model.addAttribute("orderTypes", orderTypes);
            model.addAttribute("orders", orders);
            return "orders-create";
        }
        long clientId = visit.getClient().getId();

        try {
            List<Long> ids = orders.serviceTypeIds();
            for(Long id : ids){
                Order newOrder = new Order();
                newOrder.setServiceType(orderTypeService.getById(id));
                orderService.create(newOrder, clientId, visit);
            }
            return "redirect:/garage/visits/" + visitId;
        } catch (UserMismatchException | VisitMismatchException | EntityDuplicateException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/visits/{visitId}/orders/{orderId}/delete")
    public String deleteOrder(@PathVariable long visitId,
                              @PathVariable long orderId,
                              Model model) {

        try {
            Visit visit = visitService.findById(visitId);
            long clientId = visit.getClient().getId();
            orderService.delete(clientId, visit, orderId);
            return "redirect:/garage/visits/" + visitId;
        } catch (UserMismatchException | VisitMismatchException | EntityNotFoundException e) {
            model.addAttribute("statusCode", HttpStatus.CONFLICT.getReasonPhrase());
            model.addAttribute("error", e.getMessage());
            return "error-page";
        }
    }
}
