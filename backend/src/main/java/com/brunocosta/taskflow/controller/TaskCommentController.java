package com.brunocosta.taskflow.controller;

import java.util.List;

import com.brunocosta.taskflow.dto.TaskCommentCreateRequest;
import com.brunocosta.taskflow.dto.TaskCommentResponse;
import com.brunocosta.taskflow.service.TaskCommentService;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(name = "Task Comments")
@Controller("/api/tasks/{id}/comments")
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    public TaskCommentController(TaskCommentService taskCommentService) {
        this.taskCommentService = taskCommentService;
    }

    @Post
    @Operation(summary = "Add a comment to a task")
    public HttpResponse<TaskCommentResponse> create(
            @PathVariable String id,
            @Body @Valid TaskCommentCreateRequest request) {
        return HttpResponse.created(taskCommentService.addComment(id, request));
    }

    @Get
    @Operation(summary = "List comments for a task")
    public List<TaskCommentResponse> list(@PathVariable String id) {
        return taskCommentService.listComments(id);
    }
}
