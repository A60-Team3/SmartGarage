package org.example.smartgarage.helpers;

import org.example.smartgarage.models.*;
import org.example.smartgarage.models.enums.Status;
import org.example.smartgarage.models.enums.UserRole;
import org.example.smartgarage.utils.filtering.VisitFilterOptions;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class CreationHelper {

    public static UserEntity createMockUser() {
        UserEntity user = new UserEntity();
        user.setId(1);
        user.setFirstName("firstname");
        user.setLastName("lastname");
        user.setEmail("email@email.com");
        user.setUsername("username");
        user.setPassword("password");
        user.setPhoneNumber("0888888888");
        user.setRegistered(LocalDateTime.now());
        user.setUpdated(LocalDateTime.now());
        user.setRoles(new HashSet<>());

        return user;
    }

    public static UserEntity createMockEmployee() {
        UserEntity user = createMockUser();
        user.getRoles().add(new Role(UserRole.CLERK));

        return user;
    }

    public static UserEntity createMockCustomer() {
        UserEntity user = createMockUser();
        user.getRoles().add(new Role(UserRole.CUSTOMER));

        return user;
    }


    public static VehicleBrand createMockBrand() {
        VehicleBrand brand = new VehicleBrand();
        brand.setId(1);
        brand.setBrandName("brand");

        return brand;
    }

    public static VehicleModel createMockModel() {
        VehicleModel model = new VehicleModel();
        model.setId(1);
        model.setModelName("model");

        return model;
    }

    public static VehicleYear createMockYear() {
        VehicleYear year = new VehicleYear();
        year.setId(1);
        year.setYear(2000);

        return year;
    }

    public static Vehicle createMockVehicle() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1);
        vehicle.setLicensePlate("AA1111AA");
        vehicle.setVin("AAAAAAAAAAAAAAAAA");
        vehicle.setBrandName(createMockBrand());
        vehicle.setModelName(createMockModel());
        vehicle.setYearOfCreation(createMockYear());
        vehicle.setOwner(createMockCustomer());
        vehicle.setClerk(createMockEmployee());
        vehicle.setAddedOn(LocalDate.now());
        vehicle.setUpdatedOn(LocalDate.now());

        return vehicle;
    }

    public static ServiceType createMockServiceType() {
        ServiceType serviceType = new ServiceType();
        serviceType.setId(1);
        serviceType.setServiceName("test");
        serviceType.setServicePrice(BigDecimal.valueOf(3.50));

        return serviceType;
    }

    public static Order createMockOrder() {
        Order order = new Order();
        order.setId(1);
        order.setServiceType(createMockServiceType());
        order.setAddedOn(LocalDateTime.now());
        order.setUpdatedOn(LocalDateTime.now());
        order.setVisitId(createMockVisit());

        return order;
    }

    public static Visit createMockVisit() {
        Visit visit = new Visit();
        visit.setId(1);
        visit.setScheduleDate(LocalDate.now());
        visit.setClient(createMockCustomer());
        visit.setClerk(createMockEmployee());
        visit.setStatus(Status.NOT_STARTED);
        visit.setVehicle(createMockVehicle());
        visit.setBookedOn(LocalDateTime.now());
        visit.setUpdatedOn(LocalDateTime.now());
        //visit.setServices(List.of(createMockOrder()));

        return visit;
    }

    public static EventLog createMockEventLog() {
        EventLog log = new EventLog();
        log.setId(1);
        log.setDescription("description");
        log.setVisitId(createMockVisit());

        return log;
    }

    public static ProfilePicture createMockProfilePicture() {
        return new ProfilePicture("http:/testMockTest.org/avatar.jpg");
    }
}
