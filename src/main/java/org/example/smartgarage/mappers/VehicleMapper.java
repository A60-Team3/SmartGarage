package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.models.Vehicle;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VehicleBrandService;
import org.example.smartgarage.services.contracts.VehicleModelService;
import org.example.smartgarage.services.contracts.VehicleYearService;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VehicleMapper {

    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clerk", ignore = true)
    @Mapping(target = "addedOn", ignore = true)
    @Mapping(target="brandName", expression="java(vehicleBrandService.getById(vehicleInDTO.brandId()))")
    @Mapping(target="modelName", expression="java(vehicleModelService.getById(vehicleInDTO.modelId()))")
    @Mapping(target="yearOfCreation", expression="java(vehicleYearService.getById(vehicleInDTO.yearId()))")
    @Mapping(target="owner", expression="java(userService.getById(vehicleInDTO.ownerId()))")
    Vehicle toEntity(VehicleInDTO vehicleInDTO,
                     @Context VehicleBrandService vehicleBrandService,
                     @Context VehicleModelService vehicleModelService,
                     @Context VehicleYearService vehicleYearService,
                     @Context UserService userService);

    @Mapping(target="brandName", source = "brandName.brandName")
    @Mapping(target="modelName", source = "modelName.modelName")
    @Mapping(target="year", source = "yearOfCreation.year")
    @Mapping(target="ownerName", expression = "java(vehicle.getOwner().getFirstName() + \" \" + vehicle.getOwner().getLastName())")
    VehicleOutDTO toDTO(Vehicle vehicle);

    default Page<VehicleOutDTO> vehiclesToVehicleDTOs(Page<Vehicle> vehicles) {
        List<VehicleOutDTO> dtos = vehicles.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, vehicles.getPageable(), vehicles.getTotalElements());
    }
}
