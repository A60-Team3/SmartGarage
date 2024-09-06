package org.example.smartgarage.utils;

import org.example.smartgarage.models.Role;
import org.example.smartgarage.models.UserEntity;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.repositories.contracts.*;
import org.example.smartgarage.services.contracts.AuthenticationService;
import org.example.smartgarage.services.contracts.VehicleAPIService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AuthenticationService authenticationService;
    private final VehicleAPIService vehicleAPIService;
    private final DataSource dataSource;

    private final VehicleBrandRepository vehicleBrandRepository;
    private final VehicleModelRepository vehicleModelRepository;
    private final VehicleYearRepository vehicleYearRepository;

    public DataLoader(RoleRepository roleRepository, UserRepository userRepository,
                      AuthenticationService authenticationService, VehicleAPIService vehicleAPIService, DataSource dataSource, VehicleBrandRepository vehicleBrandRepository,
                      VehicleModelRepository vehicleModelRepository, VehicleYearRepository vehicleYearRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.vehicleAPIService = vehicleAPIService;
        this.dataSource = dataSource;

        this.vehicleBrandRepository = vehicleBrandRepository;
        this.vehicleModelRepository = vehicleModelRepository;
        this.vehicleYearRepository = vehicleYearRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.findAll().isEmpty()) {
            for (UserRole userRole : UserRole.values()) {
                Role role = new Role(userRole);
                roleRepository.saveAndFlush(role);
            }
        }

        if (userRepository.findAll().isEmpty()) {
            authenticationService.registerEmployee(new UserEntity("John", "Doeson", "john.doe@example.com", "john_doe", "password123", "0000000000"));
            authenticationService.registerEmployee(new UserEntity("Jane", "Smith", "jane.smith@example.com", "jane_smith", "password123", "0000000001"));
        }

        if (vehicleBrandRepository.count() == 0 &&
                vehicleModelRepository.count() == 0 &&
                vehicleYearRepository.count() == 0) {
            vehicleAPIService.populateVehiclePropertiesDB();

            Resource resource = new ClassPathResource("data.sql");
            ResourceDatabasePopulator populator = new ResourceDatabasePopulator(resource);
            populator.execute(dataSource);
        }

        System.out.println("Initialization complete");
    }
}
