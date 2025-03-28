package com.mamydinyah.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String message;
    private String token;
    private String apiKey;

    public AuthResponse(String message) {
        this.message = message;
    }
}
