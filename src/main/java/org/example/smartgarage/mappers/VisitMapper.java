package org.example.smartgarage.mappers;

import jdk.jfr.Name;
import org.example.smartgarage.dtos.VisitOutDto;
import org.example.smartgarage.models.*;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = VehicleMapper.class
)
public interface VisitMapper {

    @Mapping(target = "exchangeRate", ignore = true)
    @Mapping(target = "currency", ignore = true)
    @Mapping(target = "bookedDate", source = "scheduleDate", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "clientName", source = "client", qualifiedByName = "customerFullName")
    @Mapping(target = "employeeName", source = "clerk", qualifiedByName = "employeeFullName")
    @Mapping(target = "history", source = "eventLogs", qualifiedByName = "history")
    @Mapping(target = "totalCost", source = "services", qualifiedByName = "totalCost")
    @Mapping(target = "services", source = "services", qualifiedByName = "services")
    VisitOutDto toDto(Visit visit);

    @Named("customerFullName")
    default String getClientName(UserEntity client) {
        return String.format("%s %s", client.getFirstName(), client.getLastName());
    }

    @Named("employeeFullName")
    default String getEmployeeName(UserEntity employee) {
        return String.format("%s %s", employee.getFirstName(), employee.getLastName());
    }

    @Named("services")
    default List<String> mapServiceToString(List<Service> services) {
        return services.stream()
                .map(Service::getServiceType)
                .map(ServiceType::toString)
                .toList();
    }

    @Named("history")
    default List<String> getHistory(List<EventLog> eventLogs) {
        return eventLogs.stream().map(EventLog::toString).toList();
    }

    @Named("totalCost")
    default BigDecimal getVisitTotalCost(List<Service> services) {
        return services.stream()
                .map(Service::getServiceType)
                .map(ServiceType::getServicePrice)
                .reduce(BigDecimal.ZERO,BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
