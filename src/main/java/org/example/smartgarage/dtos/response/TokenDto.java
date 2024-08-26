package org.example.smartgarage.dtos.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenDto(
        @JsonProperty("access_token")
        String jwtToken) {
}
