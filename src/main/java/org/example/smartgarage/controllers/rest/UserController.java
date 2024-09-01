package org.example.smartgarage.controllers.rest;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.example.smartgarage.dtos.request.UserUpdateDto;
import org.example.smartgarage.dtos.response.UserOutDto;
import org.example.smartgarage.exceptions.CustomAuthenticationException;
import org.example.smartgarage.mappers.UserMapper;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.security.CustomUserDetails;
import org.example.smartgarage.services.contracts.UserService;
import org.example.smartgarage.utils.filtering.TimeOperator;
import org.example.smartgarage.utils.filtering.UserFilterOptions;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/garage/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }


    @GetMapping()
    public ResponseEntity<List<UserOutDto>> getAllUsers(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                                        @RequestParam(value = "pageSize", defaultValue = "5") int pageSize,
                                                        @Parameter(description = "Partial or full first name")
                                                        @RequestParam(required = false) String firstName,
                                                        @Parameter(description = "Partial or full last name")
                                                        @RequestParam(required = false) String lastName,
                                                        @Parameter(description = "Partial or full email")
                                                        @RequestParam(required = false) String email,
                                                        @Parameter(description = "Partial or full username")
                                                        @RequestParam(required = false) String username,
                                                        @RequestParam(required = false) String brandName,
                                                        @Parameter(description = "Pattern - String length 17")
                                                        @RequestParam(required = false) String vehicleVin,
                                                        @Parameter(description = "Pattern - XX XXXX XX")
                                                        @RequestParam(required = false) String vehicleRegistry,
                                                        @Parameter(description = "Pattern - 0888088088")
                                                        @RequestParam(required = false) String phoneNumber,
                                                        @RequestParam(required = false) TimeOperator timeOperator,
                                                        @Parameter(description = "Pattern - YYYY-MM-DD HH:mm:ss")
                                                        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                        @RequestParam(required = false) LocalDateTime registered,
                                                        @Parameter(description = "Pattern - roleName1,roleName2,rolename3")
                                                        @RequestParam(required = false) List<UserRole> authority,
                                                        @RequestParam(required = false) Boolean isActive,
                                                        @Parameter(description = "Options - all field names")
                                                        @RequestParam(required = false) String sortBy,
                                                        @RequestParam(required = false) String sortOrder) {

        UserFilterOptions userFilterOptions =
                new UserFilterOptions(
                        firstName, lastName, email, username,
                        timeOperator, registered, isActive, authority,
                        phoneNumber, brandName, vehicleVin, vehicleRegistry, sortBy, sortOrder
                );

        Page<UserEntity> users = userService.getAll(offset, pageSize, userFilterOptions);
        List<UserOutDto> dtos = users.stream().map(userMapper::toDto).toList();

        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserOutDto> getById(@PathVariable Long userId) {
        UserEntity user = userService.getById(userId);

        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PreAuthorize("hasAnyRole('CLERK') or #userId == principal.id")
    @PutMapping("/{userId}")
    public ResponseEntity<UserOutDto> updateUser(@Valid @RequestBody UserUpdateDto dto,
                                                 @PathVariable long userId,
                                                 @AuthenticationPrincipal CustomUserDetails principal) {
        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK"));

        if (principal.getId() != userId && !hasRights) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authorized action");
        }

        UserEntity updatedUserInfo = userMapper.toEntity(dto);

        UserEntity updatedUser = userService.update(userId, updatedUserInfo);

        return ResponseEntity.ok(userMapper.toDto(updatedUser));
    }

    @PreAuthorize("hasAnyRole('CLERK')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable long userId,
                                                 @AuthenticationPrincipal CustomUserDetails principal) {
        boolean hasRights = principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(s -> s.equals("ROLE_CLERK"));

        if (!hasRights) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Only employees can delete");
        }

        userService.deleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body("Customer successfully removed from database");
    }
}
