package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.VehicleMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.services.contracts.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @GetMapping
    public ResponseEntity<Page<VehicleOutDTO>> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Vehicle> vehicles = vehicleService.getAll(offset, pageSize);
        Page<VehicleOutDTO> vehicleOutDTOPage= vehicleMapper.vehiclesToVehicleDTOs(vehicles);
        return ResponseEntity.ok(vehicleOutDTOPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleOutDTO> getById(@PathVariable long id){
        try {
            Vehicle vehicle = vehicleService.getById(id);
            VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
            return ResponseEntity.ok(vehicleOutDTO);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<VehicleOutDTO> create(@Valid @RequestBody VehicleInDTO vehicleInDTO){
        try {
            /*Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            Optional<UserEntity> userEntityOptional = userService.findByUsername(auth.getName());
            UserEntity user = userEntityOptional.get();*/

            UserEntity user = userService.findByUsername("johndoe").orElseThrow();
            Vehicle vehicle = vehicleMapper.toEntity(vehicleInDTO, vehicleBrandService, vehicleModelService, vehicleYearService, userService);
            vehicleService.create(vehicle, user);
            VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
            return ResponseEntity.ok(vehicleOutDTO);
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<VehicleOutDTO> update(@PathVariable long id, @Valid @RequestBody VehicleInDTO vehicleInDTO){
        try {
            UserEntity user = userService.findByUsername("johndoe").orElseThrow();
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable long id){

        try {
            UserEntity user = userService.findByUsername("johndoe").orElseThrow();

            vehicleService.delete(id, user);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
