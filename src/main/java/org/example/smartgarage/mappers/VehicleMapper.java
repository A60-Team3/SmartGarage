package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.VehicleInDTO;
import org.example.smartgarage.dtos.response.VehicleOutDTO;
import org.example.smartgarage.models.Vehicle;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VehicleMapper {

    Vehicle toEntity(VehicleInDTO vehicleInDTO);


    @Mapping(target="brandName", source = "brandName.brandName")
    @Mapping(target="modelName", source = "modelName.modelName")
    @Mapping(target="year", source = "yearOfCreation.year")
    @Mapping(target="ownerName", source="owner.firstName")
    VehicleOutDTO toDTO(Vehicle vehicle);

}
