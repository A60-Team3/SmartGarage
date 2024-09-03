package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.response.VehicleBrandOutDTO;
import org.example.smartgarage.models.VehicleBrand;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VehicleBrandMapper {

    VehicleBrandOutDTO toDTO(VehicleBrand vehicleBrand);

    default Page<VehicleBrandOutDTO> vehicleBrandsToVehicleBrandDTOs(Page<VehicleBrand> vehicleBrands) {
        List<VehicleBrandOutDTO> dtos = vehicleBrands.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, vehicleBrands.getPageable(), vehicleBrands.getTotalElements());
    }
}
