package com.brunocosta.taskflow.error;

import java.time.Instant;
import java.util.List;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<ApiErrorResponse>> {

    @Override
    @SuppressWarnings("rawtypes")
    public HttpResponse<ApiErrorResponse> handle(HttpRequest request, NotFoundException exception) {
        var body = new ApiErrorResponse(
                "NOT_FOUND",
                exception.getMessage(),
                List.of(),
                Instant.now());
        return HttpResponse.notFound(body);
    }
}