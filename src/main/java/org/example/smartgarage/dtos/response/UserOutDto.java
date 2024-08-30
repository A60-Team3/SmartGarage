package org.example.smartgarage.dtos.response;

import java.util.List;
import java.util.Set;

public record UserOutDto(String fullName,
                         String email,
                         String phoneNumber,
                         String registered,
                         String updated,
                         List<VehicleOutDTO> vehicles,
                         Set<String> roles) {
}
