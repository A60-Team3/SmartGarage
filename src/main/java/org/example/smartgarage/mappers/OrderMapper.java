package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.OrderInDTO;
import org.example.smartgarage.dtos.response.OrderOutDTO;
import org.example.smartgarage.models.Service;
import org.example.smartgarage.services.contracts.OrderTypeService;
import org.mapstruct.Context;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = OrderTypeMapper.class)
public interface OrderMapper {

    @Mapping(target = "serviceType", expression = "java(orderTypeService.getById(orderInDTO.serviceTypeId()))")
    Service toEntity(OrderInDTO orderInDTO,
                     @Context OrderTypeService orderTypeService);

    /*@Mapping(target = "serviceType", expression = ("java(order.getServiceType().getServiceName())"))
    @Mapping(target = "price", expression = ("java(order.getServiceType().getServicePrice())"))*/
    OrderOutDTO toDTO(Service order);

    default Page<OrderOutDTO> ordersToOrderDTOs(Page<Service> orders) {
        List<OrderOutDTO> dtos = orders.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, orders.getPageable(), orders.getTotalElements());
    }
}
