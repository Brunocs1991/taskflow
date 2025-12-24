package com.brunocosta.taskflow.error;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

import java.time.Instant;
import java.util.List;

@Singleton
@Produces
public class IllegalArgumentExceptionHandler
        implements ExceptionHandler<IllegalArgumentException, HttpResponse<ApiErrorResponse>> {

    @Override
    public HttpResponse<ApiErrorResponse> handle(HttpRequest request, IllegalArgumentException exception) {
        var body = new ApiErrorResponse(
                "BAD_REQUEST",
                exception.getMessage(),
                List.of(),
                Instant.now());
        return HttpResponse.badRequest(body);
    }
}
