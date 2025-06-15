package com.fiap.indora.dtos;

import jakarta.validation.constraints.NotBlank;

public record JwtDto(
        @NotBlank(message = "Token cannot be null or empty") String token,
        String type
) {
    public JwtDto(String token) {
       this(token, "Bearer");
    }
}
