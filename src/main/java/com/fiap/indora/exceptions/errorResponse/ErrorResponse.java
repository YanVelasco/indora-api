package com.fiap.indora.exceptions.errorResponse;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        int errorCode,
        String errorMessage,
        Map<String, String> errorsDetails
) {
}
