package com.brunocosta.taskflow.controller;



import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import com.brunocosta.taskflow.MongoTestResource;
import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCommentCreateRequest;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class TaskCommentControllerTest implements MongoTestResource {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void shouldCreateAndListComments() {
        TaskResponse task = client.toBlocking().retrieve(
                HttpRequest.POST("/api/tasks", new TaskCreateRequest("T", "D", TaskStatus.TODO)),
                TaskResponse.class
        );

        client.toBlocking().exchange(
                HttpRequest.POST("/api/tasks/" + task.id() + "/comments", new TaskCommentCreateRequest("c1"))
        );

        var list = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks/" + task.id() + "/comments"),
                Argument.listOf(TaskResponse.class)
        );

        assertFalse(list.isEmpty());
    }
}
