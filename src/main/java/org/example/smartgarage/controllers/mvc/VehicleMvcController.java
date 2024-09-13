package org.example.smartgarage.controllers.mvc;

import jakarta.servlet.http.HttpServletRequest;
import org.example.smartgarage.dtos.response.VehicleBrandOutDTO;
import org.example.smartgarage.dtos.response.VehicleModelOutDTO;
import org.example.smartgarage.mappers.VehicleBrandMapper;
import org.example.smartgarage.mappers.VehicleMapper;
import org.example.smartgarage.mappers.VehicleModelMapper;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.services.contracts.*;
import org.example.smartgarage.utils.filtering.VehicleBrandFilterOptions;
import org.example.smartgarage.utils.filtering.VehicleModelFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/garage")
public class VehicleMvcController {

    private final VehicleService vehicleService;
    private final VehicleBrandService vehicleBrandService;
    private final VehicleModelService vehicleModelService;
    private final VehicleYearService vehicleYearService;
    private final UserService userService;
    private final VehicleMapper vehicleMapper;
    private final VehicleBrandMapper vehicleBrandMapper;
    private final VehicleModelMapper vehicleModelMapper;

    public VehicleMvcController(VehicleService vehicleService, VehicleBrandService vehicleBrandService, VehicleModelService vehicleModelService, VehicleYearService vehicleYearService, UserService userService, VehicleMapper vehicleMapper, VehicleBrandMapper vehicleBrandMapper, VehicleModelMapper vehicleModelMapper) {
        this.vehicleService = vehicleService;
        this.vehicleBrandService = vehicleBrandService;
        this.vehicleModelService = vehicleModelService;
        this.vehicleYearService = vehicleYearService;
        this.userService = userService;
        this.vehicleMapper = vehicleMapper;
        this.vehicleBrandMapper = vehicleBrandMapper;
        this.vehicleModelMapper = vehicleModelMapper;
    }

    @ModelAttribute("requestURI")
    public String requestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }

    @GetMapping("/brands")
    public String getBrands(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @ModelAttribute("vehicleBrandFilterOptions")VehicleBrandFilterOptions filterOptions,
                            Model model){

        filterOptions.removeInvalid();

        Page<VehicleBrand> brands = vehicleBrandService.getAll(pageIndex,pageSize, filterOptions);
        //Page<VehicleBrandOutDTO> brandOutDTOS = vehicleBrandMapper.vehicleBrandsToVehicleBrandDTOs(brands);

        model.addAttribute("brands", brands);
        model.addAttribute("currentPage", brands.getNumber() + 1);
        model.addAttribute("totalPages", brands.getTotalPages());
        return "brands";
    }

    @GetMapping("/brands/{brandId}/models")
    public String getModels(@PathVariable long brandId,
                            @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @ModelAttribute("vehicleModelFilterOptions")VehicleModelFilterOptions filterOptions,
                            Model model){

        filterOptions.removeInvalid();
        VehicleBrand brand = vehicleBrandService.getById(brandId);
        Page<VehicleModel> models = vehicleModelService.getByBrand(brand, pageIndex, pageSize, filterOptions);
        Page<VehicleModelOutDTO> modelOutDTOS = vehicleModelMapper.vehicleModelsToVehicleModelDTOs(models);

        model.addAttribute("brandId", brandId);
        model.addAttribute("currentPage", models.getNumber() + 1);
        model.addAttribute("totalPages", models.getTotalPages());
        model.addAttribute("models", modelOutDTOS);
        return "models";
    }
}
