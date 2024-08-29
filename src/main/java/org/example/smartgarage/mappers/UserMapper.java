package org.example.smartgarage.mappers;

import org.example.smartgarage.dtos.request.CustomerRegistrationDto;
import org.example.smartgarage.dtos.request.EmployeeRegistrationDto;
import org.example.smartgarage.dtos.request.UserUpdateDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    UserEntity toEntity(CustomerRegistrationDto customerRegistrationDto);

    UserEntity toEntity(EmployeeRegistrationDto employeeRegistrationDto);
    UserEntity toEntity(UserUpdateDto userUpdateDto);

    @Mapping(target = "roles", expression = "java(mapUserRolesToString(user.getRoles()))")
    @Mapping(target = "fullName", expression = "java(user.getFirstName() + \" \" + user.getLastName())")
    @Mapping(target = "registered", source = "registered", dateFormat = "yyyy-MM-dd")
    @Mapping(target = "updated", source = "updated", dateFormat = "yyyy-MM-dd")
    UserOutDto toDto(UserEntity user);

    default Set<String> mapUserRolesToString(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRole)
                .map(UserRole::name)
                .collect(Collectors.toSet());
    }
}
