package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.VehicleMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.*;
import org.mapstruct.control.MappingControl;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/garage/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final VehicleYearService vehicleYearService;
    private final UserService userService;
    private final VehicleMapper vehicleMapper;

    public VehicleController(VehicleService vehicleService, VehicleBrandService vehicleBrandService, VehicleModelService vehicleModelService, VehicleYearService vehicleYearService, UserService userService, VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleModelService = vehicleModelService;
        this.vehicleYearService = vehicleYearService;
        this.userService = userService;
        this.vehicleMapper = vehicleMapper;
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping
    public ResponseEntity<?> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @AuthenticationPrincipal CustomUserDetails principal){
        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));
        if (!hasRights) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
        }
        Page<Vehicle> vehicles = vehicleService.getAll(offset, pageSize);
        Page<VehicleOutDTO> vehicleOutDTOPage= vehicleMapper.vehiclesToVehicleDTOs(vehicles);
        return ResponseEntity.ok(vehicleOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id,
                                     @AuthenticationPrincipal CustomUserDetails principal){
        try {
            boolean hasRights = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));
            if (!hasRights) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
            }
            Vehicle vehicle = vehicleService.getById(id);
            VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
            return ResponseEntity.ok(vehicleOutDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody VehicleInDTO vehicleInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal){
        try {
            boolean hasRights = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));
            if (!hasRights) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
            }
            UserEntity user = userService.getById(principal.getId());
            Vehicle vehicle = vehicleMapper.toEntity(vehicleInDTO, vehicleBrandService, vehicleModelService, vehicleYearService, userService);
            vehicleService.create(vehicle, user);
            VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
            return ResponseEntity.ok(vehicleOutDTO);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @Valid @RequestBody VehicleInDTO vehicleInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal){
        try {
            boolean hasRights = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));
            if (!hasRights) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
            }
            UserEntity user = userService.getById(principal.getId());
            Vehicle vehicle = vehicleMapper.toEntity(vehicleInDTO, vehicleBrandService, vehicleModelService, vehicleYearService, userService);
            Vehicle updated = vehicleService.update(id, vehicle, user);
            VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(updated);
            return ResponseEntity.ok(vehicleOutDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id,
                                    @AuthenticationPrincipal CustomUserDetails principal){

        try {
            boolean hasRights = principal.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(s -> s.equals("ROLE_CLERK") || s.equals("ROLE_MECHANIC"));
            if (!hasRights) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Only owner can modify his info");
            }
            UserEntity user = userService.getById(principal.getId());

            vehicleService.delete(id, user);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
