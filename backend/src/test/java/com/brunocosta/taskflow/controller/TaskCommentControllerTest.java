package com.brunocosta.taskflow.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCommentCreateRequest;
import com.brunocosta.taskflow.dto.TaskCommentResponse;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;
import com.brunocosta.taskflow.service.TaskCommentService;
import com.brunocosta.taskflow.service.TaskService;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class TaskCommentControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    TaskService taskService;

    @Inject
    TaskCommentService taskCommentService;

    @BeforeEach
    void setup() {
        reset(taskService, taskCommentService);
    }

    @Test
    void shouldCreateAndListComments() {
        Instant now = Instant.now();
        TaskResponse task = new TaskResponse("id1", "T", "D", TaskStatus.TODO, now, now);
        TaskCommentResponse comment = new TaskCommentResponse("c1", "id1", "c1", now);

        when(taskService.create(eq(new TaskCreateRequest("T", "D", TaskStatus.TODO))))
                .thenReturn(task);
        when(taskCommentService.addComment(eq("id1"), eq(new TaskCommentCreateRequest("c1"))))
                .thenReturn(comment);
        when(taskCommentService.listComments("id1"))
                .thenReturn(List.of(comment));

        TaskResponse taskResp = client.toBlocking().retrieve(
                HttpRequest.POST("/api/tasks", new TaskCreateRequest("T", "D", TaskStatus.TODO)),
                TaskResponse.class
        );

        client.toBlocking().exchange(
                HttpRequest.POST("/api/tasks/" + taskResp.id() + "/comments", new TaskCommentCreateRequest("c1"))
        );

        var list = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks/" + taskResp.id() + "/comments"),
                Argument.listOf(TaskCommentResponse.class)
        );

        assertFalse(list.isEmpty());
    }

}
