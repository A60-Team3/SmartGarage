package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.UserUpdateDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = VehicleMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity toEntity(CustomerRegistrationDto customerRegistrationDto);

    UserEntity toEntity(EmployeeRegistrationDto employeeRegistrationDto);

    UserEntity toEntity(UserUpdateDto userUpdateDto);

    @Mapping(target = "roles", source = "roles", qualifiedByName = "rolesToString")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "registered", source = "registered", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "updated", source = "updated", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "vehicles", source = "vehicles")
    UserOutDto toDto(UserEntity user);

    @Named("rolesToString")
    default Set<String> mapUserRolesToString(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRole)
                .map(UserRole::name)
                .collect(Collectors.toSet());
    }
}
