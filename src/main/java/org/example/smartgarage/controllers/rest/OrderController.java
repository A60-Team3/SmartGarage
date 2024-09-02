package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.OrderInDTO;
import org.example.smartgarage.dtos.response.OrderOutDTO;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.exceptions.UserMismatchException;
import org.example.smartgarage.mappers.OrderMapper;
import org.example.smartgarage.models.Order;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Visit;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderService;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VisitService;
import org.example.smartgarage.utils.filtering.OrderFilterOptions;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/garage")
public class OrderController {

    private final OrderService orderService;
    private final OrderTypeService orderTypeService;
    private final UserService userService;
    private final VisitService visitService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderTypeService orderTypeService, UserService userService,
                           VisitService visitService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderTypeService = orderTypeService;
        this.userService = userService;
        this.visitService = visitService;
        this.orderMapper = orderMapper;
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/service-history")
    public ResponseEntity<?> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {


        Page<Order> orders = orderService.getAll(offset, pageSize);
        Page<OrderOutDTO> orderOutDTOPage = orderMapper.ordersToOrderDTOs(orders);
        return ResponseEntity.ok(orderOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/users/{userId}/visits/{visitId}/orders")
    public ResponseEntity<?> getAllByVisit(@PathVariable long userId,
                                           @PathVariable long visitId,
                                           @RequestParam(value = "offset", defaultValue = "0") int offset,
                                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {



            Visit visit = visitService.findById(visitId);
            Page<Order> orders = orderService.getAllByVisit(userId, visit, offset, pageSize);
            Page<OrderOutDTO> orderOutDTOPage = orderMapper.ordersToOrderDTOs(orders);
            return ResponseEntity.ok(orderOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC') or #userId == principal.id")
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<?> getAllByUser(@PathVariable long userId,
                                          @RequestParam(value = "offset", defaultValue = "0") int offset,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String vehicle,
                                          @RequestParam(required = false) TimeOperator condition,
                                          @RequestParam(required = false) LocalDateTime date,
                                          @RequestParam(required = false) String sortBy,
                                          @RequestParam(required = false) String sortOrder,
                                          @AuthenticationPrincipal CustomUserDetails principal) {


        OrderFilterOptions orderFilterOptions = new OrderFilterOptions(vehicle, date, condition, sortBy, sortOrder);
        UserEntity user = userService.getById(userId);
        Page<Order> orders = orderService.getAllByUser(user, offset, pageSize, orderFilterOptions);
        Page<OrderOutDTO> orderOutDTOPage = orderMapper.ordersToOrderDTOs(orders);
        return ResponseEntity.ok(orderOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/users/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> getById(@PathVariable long userId,
                                     @PathVariable long visitId,
                                     @PathVariable long orderId) {


            Visit visit = visitService.findById(visitId);
            Order order = orderService.getById(userId, visit, orderId);
            OrderOutDTO orderOutDTO = orderMapper.toDTO(order);
            return ResponseEntity.ok(orderOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping("/users/{userId}/visits/{visitId}/orders")
    public ResponseEntity<?> create(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @Valid @RequestBody OrderInDTO orderInDTO) {


            Visit visit = visitService.findById(visitId);
            Order order = orderMapper.toEntity(orderInDTO, orderTypeService);
            orderService.create(order, userId, visit);
            OrderOutDTO orderOutDTO = orderMapper.toDTO(order);
            return ResponseEntity.ok(orderOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PutMapping("/users/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> update(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @PathVariable long orderId,
                                    @Valid @RequestBody OrderInDTO orderInDTO) {

        Visit visit = visitService.findById(visitId);
        Order order = orderMapper.toEntity(orderInDTO, orderTypeService);
        Order updated = orderService.update(orderId, order, userId, visit);
        OrderOutDTO orderOutDTO = orderMapper.toDTO(updated);
        return ResponseEntity.ok(orderOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @DeleteMapping("/users/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> delete(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @PathVariable long orderId) {


        Visit visit = visitService.findById(visitId);
        orderService.delete(userId, visit, orderId);
        return ResponseEntity.ok("Order deleted successfully");
    }
}
