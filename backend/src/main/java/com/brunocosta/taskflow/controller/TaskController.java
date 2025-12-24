package com.brunocosta.taskflow.controller;

import java.util.List;

import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;
import com.brunocosta.taskflow.service.TaskService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@Tag(name = "Tasks")
@Controller("/api/tasks")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @Post
    @Operation(summary = "Create a new task")
    public HttpResponse<TaskResponse> create(@Body @Valid TaskCreateRequest request) {
        return HttpResponse.created(service.create(request));
    }

    @Get("/{id}")
    @Operation(summary = "Get a task by id")
    public TaskResponse getById(@PathVariable String id) {
        return service.getById(id);
    }

    @Put("/{id}")
    @Operation(summary = "Update a task by id")
    public TaskResponse update(@PathVariable String id, @Body @Valid TaskCreateRequest request) {
        return service.update(id, request);
    }

    @Delete("/{id}")
    @Operation(summary = "Delete a task by id")
    public HttpResponse<?> delete(@PathVariable String id) {
        service.delete(id);
        return HttpResponse.noContent();
    }

    @Get
    @Operation(summary = "List tasks with optional pagination and filters")
    public List<TaskResponse> list(
            @QueryValue(defaultValue = "0") @Min(0) Integer page,
            @QueryValue(defaultValue = "10") @Min(1) @Max(50) Integer size,
            @Nullable @QueryValue TaskStatus status,
            @Nullable @QueryValue String q) {
        return service.list(page, size, status, q);
    }
}
