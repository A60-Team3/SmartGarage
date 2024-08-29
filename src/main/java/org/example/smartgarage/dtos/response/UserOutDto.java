package org.example.smartgarage.dtos.response;

import java.util.Set;

public record UserOutDto(String fullName,
                         String email,
                         String registered,
                         String updated,
                         Set<String> roles) {
}
