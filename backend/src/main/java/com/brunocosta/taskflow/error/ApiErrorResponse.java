package com.brunocosta.taskflow.error;

import java.time.Instant;
import java.util.List;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record ApiErrorResponse(
        String error,
        String message,
        List<ApiFieldError> details,
        Instant timestamp
) {
    @Serdeable
    public record ApiFieldError(String field, String message) {}
}