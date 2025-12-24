package com.brunocosta.taskflow.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import org.junit.jupiter.api.Test;

import com.brunocosta.taskflow.MongoTestResource;
import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.HttpClient;
import io.micronaut.core.type.Argument;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class TaskControllerTest implements MongoTestResource {

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void fullFlow_create_get_update_delete() {
        var createReq = new TaskCreateRequest("Task A", "Desc", TaskStatus.TODO);
        TaskResponse created = client.toBlocking().retrieve(
                HttpRequest.POST("/api/tasks", createReq),
                TaskResponse.class);

        assertNotNull(created.id());
        assertEquals("Task A", created.title());

        // GET
        TaskResponse fetched = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks/" + created.id()),
                TaskResponse.class);
        assertEquals(created.id(), fetched.id());

        // UPDATE
        var updateReq = new TaskCreateRequest("Task A2", "Desc2", TaskStatus.DOING);
        TaskResponse updated = client.toBlocking().retrieve(
                HttpRequest.PUT("/api/tasks/" + created.id(), updateReq),
                TaskResponse.class);
        assertEquals("Task A2", updated.title());
        assertEquals(TaskStatus.DOING, updated.status());

        // LIST
        var list = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks?page=0&size=10"),
                Argument.listOf(TaskResponse.class));
        assertFalse(list.isEmpty());

        // DELETE
        var resp = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/tasks/" + created.id()));
        assertEquals(204, resp.getStatus().getCode());
    }

    @Test
    void list_shouldFilterByStatus_andSearch() {
        client.toBlocking().retrieve(HttpRequest.POST("/api/tasks",
                new TaskCreateRequest("Micronaut", "Estudar", TaskStatus.TODO)));

        client.toBlocking().retrieve(HttpRequest.POST("/api/tasks",
                new TaskCreateRequest("Kafka", "Mensageria", TaskStatus.DONE)));

        // status filter
        var todo = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks?status=TODO"),
                Argument.listOf(TaskResponse.class));
        assertFalse(todo.isEmpty());

        // search q
        var q = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks?q=Kafka"),
                Argument.listOf(TaskResponse.class));
        assertFalse(q.isEmpty());
    }
}
