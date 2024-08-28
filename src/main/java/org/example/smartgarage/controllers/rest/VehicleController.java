package org.example.smartgarage.controllers.rest;

import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.VehicleMapper;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.services.contracts.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/garage/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    public VehicleController(VehicleService vehicleService, VehicleMapper vehicleMapper) {
        this.vehicleService = vehicleService;
        this.vehicleMapper = vehicleMapper;
    }

    @GetMapping
    public ResponseEntity<Page<Vehicle>> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        Page<Vehicle> vehicles = vehicleService.getAll(offset, pageSize);
        return ResponseEntity.ok(vehicles);
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

}
