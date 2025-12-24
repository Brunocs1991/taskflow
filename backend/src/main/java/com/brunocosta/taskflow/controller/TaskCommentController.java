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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller("/api/tasks/{id}/comments")
@RequiredArgsConstructor
public class TaskCommentController {

    private final TaskCommentService taskCommentService;

    @Post
    public HttpResponse<TaskCommentResponse> create(
            @PathVariable String id,
            @Body @Valid TaskCommentCreateRequest request) {
        return HttpResponse.created(taskCommentService.addComment(id, request));
    }

    @Get
    public List<TaskCommentResponse> list(@PathVariable String id) {
        return taskCommentService.listComments(id);
    }
}
