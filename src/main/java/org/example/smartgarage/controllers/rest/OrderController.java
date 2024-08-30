package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.OrderInDTO;
import org.example.smartgarage.dtos.response.OrderOutDTO;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.OrderMapper;
import org.example.smartgarage.models.Service;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderService;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/garage/users")
public class OrderController {

    private final OrderService orderService;
    private final OrderTypeService orderTypeService;
    private final VisitService visitService;
    private final UserService userService;
    private final OrderMapper orderMapper;

    @Autowired
    public OrderController(OrderService orderService, OrderTypeService orderTypeService, VisitService visitService, UserService userService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.orderTypeService = orderTypeService;
        this.visitService = visitService;
        this.userService = userService;
        this.orderMapper = orderMapper;
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/{userId}/visits/{visitId}/orders")
    public ResponseEntity<?> getAll(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @AuthenticationPrincipal CustomUserDetails principal){

        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));

        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }

        Page<Service> orders = orderService.getAll(offset, pageSize);
        Page<OrderOutDTO> orderOutDTOPage = orderMapper.ordersToOrderDTOs(orders);
        return ResponseEntity.ok(orderOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> getById(@PathVariable long userId,
                                     @PathVariable long visitId,
                                     @PathVariable long orderId,
                                     @AuthenticationPrincipal CustomUserDetails principal){

        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));

        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }

        Service order = orderService.getById(orderId);
        OrderOutDTO orderOutDTO = orderMapper.toDTO(order);
        return ResponseEntity.ok(orderOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping("/{userId}/visits/{visitId}/orders")
    public ResponseEntity<?> create(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @Valid @RequestBody OrderInDTO orderInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal){

        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));

        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }

        Service order = orderMapper.toEntity(orderInDTO, orderTypeService);
        orderService.create(order, visitService.findById(visitId));
        OrderOutDTO orderOutDTO = orderMapper.toDTO(order);
        return ResponseEntity.ok(orderOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PutMapping("/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> update(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @PathVariable long orderId,
                                    @Valid @RequestBody OrderInDTO orderInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal){

        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));

        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }

        try {
            Service order = orderMapper.toEntity(orderInDTO, orderTypeService);
            Service updated = orderService.update(orderId, order, visitService.findById(visitId));
            OrderOutDTO orderOutDTO = orderMapper.toDTO(updated);
            return ResponseEntity.ok(orderOutDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @DeleteMapping("/{userId}/visits/{visitId}/orders/{orderId}")
    public ResponseEntity<?> delete(@PathVariable long userId,
                                    @PathVariable long visitId,
                                    @PathVariable long orderId,
                                    @AuthenticationPrincipal CustomUserDetails principal){
        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));

        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }

        try {
            UserEntity user = userService.getById(principal.getId());
            orderService.delete(orderId, user);
            return ResponseEntity.ok("Order deleted successfully");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
