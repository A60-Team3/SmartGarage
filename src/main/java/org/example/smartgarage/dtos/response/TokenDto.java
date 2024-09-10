package org.example.smartgarage.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenDto(
        @JsonProperty("token")
        String jwtToken) {
}
