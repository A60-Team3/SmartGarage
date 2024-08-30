package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.OrderTypeInDTO;
import org.example.smartgarage.dtos.response.OrderTypeOutDTO;
import org.example.smartgarage.models.ServiceType;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface OrderTypeMapper {

    ServiceType toEntity(OrderTypeInDTO orderTypeInDTO);

    OrderTypeOutDTO toDTO(ServiceType serviceType);

    default Page<OrderTypeOutDTO> orderTypesToOrderTypeDTOs(Page<ServiceType> orderTypes) {
        List<OrderTypeOutDTO> dtos = orderTypes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, orderTypes.getPageable(), orderTypes.getTotalElements());
    }
}
