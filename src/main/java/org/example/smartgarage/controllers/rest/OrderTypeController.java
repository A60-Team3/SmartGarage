package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.OrderTypeInDTO;
import org.example.smartgarage.dtos.response.OrderTypeOutDTO;
import org.example.smartgarage.mappers.OrderTypeMapper;
import org.example.smartgarage.models.ServiceType;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.example.smartgarage.utils.filtering.OrderTypeFilterOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/garage/services")
public class OrderTypeController {

    private final OrderTypeService orderTypeService;
    private final OrderTypeMapper orderTypeMapper;

    @Autowired
    public OrderTypeController(OrderTypeService orderTypeService, OrderTypeMapper orderTypeMapper) {
        this.orderTypeService = orderTypeService;
        this.orderTypeMapper = orderTypeMapper;
    }

    @GetMapping
    public ResponseEntity<Page<OrderTypeOutDTO>> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) BigDecimal price,
                                                        @RequestParam(required = false) String sortBy,
                                                        @RequestParam(required = false) String sortOrder) {

        OrderTypeFilterOptions orderTypeFilterOptions = new OrderTypeFilterOptions(name, price, sortBy, sortOrder);
        Page<ServiceType> orderTypes = orderTypeService.getAll(offset, pageSize, orderTypeFilterOptions);
        Page<OrderTypeOutDTO> orderTypeOutDTOS = orderTypeMapper.orderTypesToOrderTypeDTOs(orderTypes);

        return ResponseEntity.ok(orderTypeOutDTOS);

    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderTypeOutDTO> getById(@PathVariable long id) {

        ServiceType orderType = orderTypeService.getById(id);
        OrderTypeOutDTO orderTypeOutDTO = orderTypeMapper.toDTO(orderType);
        return ResponseEntity.ok(orderTypeOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody OrderTypeInDTO orderTypeInDTO) {

        ServiceType orderType = orderTypeMapper.toEntity(orderTypeInDTO);
        orderTypeService.create(orderType);
        OrderTypeOutDTO orderTypeOutDTO = orderTypeMapper.toDTO(orderType);
        return ResponseEntity.ok(orderTypeOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @Valid @RequestBody OrderTypeInDTO orderTypeInDTO) {

        ServiceType orderType = orderTypeMapper.toEntity(orderTypeInDTO);
        ServiceType updated = orderTypeService.update(id, orderType);
        OrderTypeOutDTO orderTypeOutDTO = orderTypeMapper.toDTO(updated);
        return ResponseEntity.ok(orderTypeOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {

        orderTypeService.delete(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
