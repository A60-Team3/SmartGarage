package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.VisitInDto;
import org.example.smartgarage.dtos.response.VisitOutDto;
import org.example.smartgarage.models.*;
import org.example.smartgarage.models.enums.Status;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.services.contracts.VehicleService;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = VehicleMapper.class,
        imports = {UserService.class, VehicleService.class, Status.class}
)
public abstract class VisitMapper {
    @Autowired
    protected UserService userService;
    @Autowired
    protected VehicleService vehicleService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scheduleDate", source = "bookedDate")
    @Mapping(target = "clerk", ignore = true)
    @Mapping(target = "client", expression = "java(userService.getById(dto.customerId()))")
    @Mapping(target = "vehicle", expression = "java(vehicleService.getById(dto.vehicleId()))")
    @Mapping(target = "services", ignore = true)
    @Mapping(target = "status", expression = ("java(Status.NOT_STARTED)"))
    @Mapping(target = "bookedOn", ignore = true)
    @Mapping(target = "updatedOn", ignore = true)
    @Mapping(target = "eventLogs", ignore = true)
    public abstract Visit toEntity(VisitInDto dto);

    @Mapping(target = "exchangeRate", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "bookedDate", source = "scheduleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "clientName", source = "client", qualifiedByName = "customerFullName")
    @Mapping(target = "employeeName", source = "clerk", qualifiedByName = "employeeFullName")
    @Mapping(target = "history", source = "eventLogs", qualifiedByName = "history")
    @Mapping(target = "totalCost", source = "services", qualifiedByName = "totalCost")
    @Mapping(target = "services", source = "services", qualifiedByName = "services")
    public abstract VisitOutDto toDto(Visit visit);

    @Named("customerFullName")
    public String getClientName(UserEntity client) {
        return String.format("%s %s", client.getFirstName(), client.getLastName());
    }

    @Named("employeeFullName")
    public String getEmployeeName(UserEntity employee) {
        return String.format("%s %s", employee.getFirstName(), employee.getLastName());
    }

    @Named("services")
    public List<String> mapServiceToString(List<Order> orders) {
        return orders.stream()
                .map(Order::getServiceType)
                .map(ServiceType::toString)
                .toList();
    }

    @Named("history")
    public List<String> getHistory(List<EventLog> eventLogs) {
        return eventLogs.stream().map(EventLog::toString).toList();
    }

    @Named("totalCost")
    public BigDecimal getVisitTotalCost(List<Order> orders) {
        return orders.stream()
                .map(Order::getServiceType)
                .map(ServiceType::getServicePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
