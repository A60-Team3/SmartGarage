package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.response.VehicleBrandOutDTO;
import org.example.smartgarage.dtos.response.VehicleModelOutDTO;
import org.example.smartgarage.models.VehicleBrand;
import org.example.smartgarage.models.VehicleModel;
import org.example.smartgarage.models.VehicleYear;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VehicleModelMapper {

    VehicleModelOutDTO toDTO(VehicleModel vehicleModel);

    default Integer yearToInteger(VehicleYear year){
        return (year == null) ? null : year.getYear();
    }

    default Page<VehicleModelOutDTO> vehicleModelsToVehicleModelDTOs(Page<VehicleModel> vehicleModels) {
        List<VehicleModelOutDTO> dtos = vehicleModels.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, vehicleModels.getPageable(), vehicleModels.getTotalElements());
    }
}
