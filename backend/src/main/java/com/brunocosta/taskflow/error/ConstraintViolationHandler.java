package com.brunocosta.taskflow.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import jakarta.inject.Singleton;
import jakarta.validation.ConstraintViolationException;

import java.time.Instant;
import java.util.List;

@Singleton
@Produces
public class ConstraintViolationHandler implements ExceptionHandler<ConstraintViolationException, HttpResponse<ApiErrorResponse>> {

    @Override
    @SuppressWarnings("rawtypes")
    public HttpResponse<ApiErrorResponse> handle(HttpRequest request, ConstraintViolationException exception) {

        List<ApiErrorResponse.ApiFieldError> details = exception.getConstraintViolations()
                .stream()
                .map(v -> new ApiErrorResponse.ApiFieldError(
                        v.getPropertyPath() != null ? v.getPropertyPath().toString() : "unknown",
                        v.getMessage()
                ))
                .toList();

        var body = new ApiErrorResponse(
                "VALIDATION_ERROR",
                "Campos inválidos",
                details,
                Instant.now()
        );

        return HttpResponse.badRequest(body);
    }
}
