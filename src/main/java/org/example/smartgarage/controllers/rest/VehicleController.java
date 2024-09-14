package org.example.smartgarage.controllers.rest;

import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VehicleBrandOutDTO;
import org.example.smartgarage.dtos.response.VehicleModelOutDTO;
import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.exceptions.EntityDuplicateException;
import org.example.smartgarage.exceptions.EntityNotFoundException;
import org.example.smartgarage.mappers.VehicleBrandMapper;
import org.example.smartgarage.mappers.VehicleMapper;
import org.example.smartgarage.mappers.VehicleModelMapper;
import org.example.smartgarage.models.*;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.*;
import org.example.smartgarage.utils.filtering.VehicleBrandFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleModelFilterOptions;
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
@RequestMapping("/api/garage")
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final VehicleYearService vehicleYearService;
    private final UserService userService;
    private final VehicleMapper vehicleMapper;
    private final VehicleBrandMapper vehicleBrandMapper;
    private final VehicleModelMapper vehicleModelMapper;

    public VehicleController(VehicleService vehicleService, VehicleBrandService vehicleBrandService, VehicleModelService vehicleModelService, VehicleYearService vehicleYearService, UserService userService, VehicleMapper vehicleMapper, VehicleBrandMapper vehicleBrandMapper, VehicleModelMapper vehicleModelMapper) {
        this.vehicleService = vehicleService;
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleModelService = vehicleModelService;
        this.vehicleYearService = vehicleYearService;
        this.userService = userService;
        this.vehicleMapper = vehicleMapper;
        this.vehicleBrandMapper = vehicleBrandMapper;
        this.vehicleModelMapper = vehicleModelMapper;
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) String name,
                                          @RequestParam(required = false) String sortOrder) {

        VehicleBrandFilterOptions vehicleBrandFilterOptions = new VehicleBrandFilterOptions(name, sortOrder);
        Page<VehicleBrand> vehicleBrands = vehicleBrandService.getAll(offset, pageSize, vehicleBrandFilterOptions);
        Page<VehicleBrandOutDTO> vehicleBrandOutDTOS = vehicleBrandMapper.vehicleBrandsToVehicleBrandDTOs(vehicleBrands);
        return ResponseEntity.ok(vehicleBrandOutDTOS);
    }

    @GetMapping("/brands/{brandId}/models")
    public ResponseEntity<?> getModels(@PathVariable long brandId,
                                       @RequestParam(value = "offset", defaultValue = "0") int offset,
                                       @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                       @RequestParam(required = false) String name,
                                       @RequestParam(required = false) Integer year,
                                       @RequestParam(required = false) String sortOrder) {
        VehicleModelFilterOptions vehicleModelFilterOptions = new VehicleModelFilterOptions(name, year, sortOrder);
        VehicleBrand vehicleBrand = vehicleBrandService.getById(brandId);
        Page<VehicleModel> vehicleModels = vehicleModelService.getByBrand(vehicleBrand, offset, pageSize, vehicleModelFilterOptions);
        Page<VehicleModelOutDTO> vehicleModelOutDTOS = vehicleModelMapper.vehicleModelsToVehicleModelDTOs(vehicleModels);
        return ResponseEntity.ok(vehicleModelOutDTOS);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/vehicles")
    public ResponseEntity<?> getAll(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                    @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                    @RequestParam(required = false) String owner,
                                    @RequestParam(required = false) String sortBy,
                                    @RequestParam(required = false) String sortOrder) {

        VehicleFilterOptions vehicleFilterOptions = new VehicleFilterOptions(owner, null, null, null, sortBy, sortOrder);
        Page<Vehicle> vehicles = vehicleService.getAll(offset, pageSize, vehicleFilterOptions);
        Page<VehicleOutDTO> vehicleOutDTOPage = vehicleMapper.vehiclesToVehicleDTOs(vehicles);
        return ResponseEntity.ok(vehicleOutDTOPage);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {

        Vehicle vehicle = vehicleService.getById(id);
        VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
        return ResponseEntity.ok(vehicleOutDTO);
    }

    @GetMapping("/vehicles/vin")
    public ResponseEntity<?> findVehicle(@RequestParam(required = false) String licensePlate,
                                         @RequestParam(required = false) String vin,
                                         @RequestParam(required = false) String brandName) {

        VehicleFilterOptions vehicleFilterOptions =
                new VehicleFilterOptions(
                        null,
                        (brandName.isBlank() ? null : brandName),
                        (vin.isBlank() ? null : vin),
                        (licensePlate.isBlank() ? null : licensePlate),
                        null, null);

        List<Vehicle> result = vehicleService.findFiltered(vehicleFilterOptions);

        List<VehicleOutDTO> output = result.stream().map(vehicleMapper::toDTO).toList();

        return ResponseEntity.ok(output);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PostMapping("/vehicles")
    public ResponseEntity<?> create(@Valid @RequestBody VehicleInDTO vehicleInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        UserEntity user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleMapper.toEntity(vehicleInDTO, vehicleBrandService, vehicleModelService, vehicleYearService, userService);
        vehicleService.create(vehicle, user);
        VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(vehicle);
        return ResponseEntity.ok(vehicleOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<?> update(@PathVariable long id,
                                    @Valid @RequestBody VehicleInDTO vehicleInDTO,
                                    @AuthenticationPrincipal CustomUserDetails principal) {

        UserEntity user = userService.getById(principal.getId());
        Vehicle vehicle = vehicleMapper.toEntity(vehicleInDTO, vehicleBrandService, vehicleModelService, vehicleYearService, userService);
        Vehicle updated = vehicleService.update(id, vehicle, user);
        VehicleOutDTO vehicleOutDTO = vehicleMapper.toDTO(updated);
        return ResponseEntity.ok(vehicleOutDTO);
    }

    @PreAuthorize("hasAnyRole('CLERK', 'MECHANIC')")
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> delete(@PathVariable long id) {
        vehicleService.delete(id);
        return ResponseEntity.ok("Vehicle deleted successfully");
    }
}
