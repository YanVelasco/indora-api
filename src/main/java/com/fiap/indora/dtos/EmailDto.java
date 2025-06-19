package com.fiap.indora.dtos;

public record EmailDto(
        String email,
        Boolean primary,
        Boolean verified,
        String visibility
) {
}
