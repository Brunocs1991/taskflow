package com.brunocosta.taskflow.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;
import com.brunocosta.taskflow.service.TaskService;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.HttpClient;
import io.micronaut.core.type.Argument;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class TaskControllerTest {

    @Inject
    @Client("/")
    HttpClient client;

    @Inject
    TaskService taskService;

    @BeforeEach
    void setup() {
        reset(taskService);
    }

    @Test
    void fullFlow_create_get_update_delete() {
        Instant now = Instant.now();
        TaskResponse created = new TaskResponse("id1", "Task A", "Desc", TaskStatus.TODO, now, now);
        TaskResponse updated = new TaskResponse("id1", "Task A2", "Desc2", TaskStatus.DOING, now, now);

        when(taskService.create(eq(new TaskCreateRequest("Task A", "Desc", TaskStatus.TODO))))
                .thenReturn(created);
        when(taskService.getById("id1")).thenReturn(created);
        when(taskService.update(eq("id1"), eq(new TaskCreateRequest("Task A2", "Desc2", TaskStatus.DOING))))
                .thenReturn(updated);
        when(taskService.list(eq(0), eq(10), isNull(), isNull())).thenReturn(List.of(updated));
        doNothing().when(taskService).delete("id1");

        var createReq = new TaskCreateRequest("Task A", "Desc", TaskStatus.TODO);
        TaskResponse createdResp = client.toBlocking().retrieve(
                HttpRequest.POST("/api/tasks", createReq),
                TaskResponse.class);

        assertNotNull(createdResp.id());
        assertEquals("Task A", createdResp.title());

        // GET
        TaskResponse fetched = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks/" + createdResp.id()),
                TaskResponse.class);
        assertEquals(createdResp.id(), fetched.id());

        // UPDATE
        var updateReq = new TaskCreateRequest("Task A2", "Desc2", TaskStatus.DOING);
        TaskResponse updated1 = client.toBlocking().retrieve(
                HttpRequest.PUT("/api/tasks/" + createdResp.id(), updateReq),
                TaskResponse.class);
        assertEquals("Task A2", updated1.title());
        assertEquals(TaskStatus.DOING, updated.status());

        // LIST
        var list = client.toBlocking().retrieve(
                HttpRequest.GET("/api/tasks?page=0&size=10"),
                Argument.listOf(TaskResponse.class));
        assertFalse(list.isEmpty());

        // DELETE
        var resp = client.toBlocking().exchange(
                HttpRequest.DELETE("/api/tasks/" + createdResp.id()));
        assertEquals(204, resp.getStatus().getCode());
    }

    @Test
    void list_shouldFilterByStatus_andSearch() {
        Instant now = Instant.now();
        when(taskService.create(eq(new TaskCreateRequest("Micronaut", "Estudar", TaskStatus.TODO))))
                .thenReturn(new TaskResponse("id1", "Micronaut", "Estudar", TaskStatus.TODO, now, now));
        when(taskService.create(eq(new TaskCreateRequest("Kafka", "Mensageria", TaskStatus.DONE))))
                .thenReturn(new TaskResponse("id2", "Kafka", "Mensageria", TaskStatus.DONE, now, now));
        when(taskService.list(eq(0), eq(10), eq(TaskStatus.TODO), isNull()))
                .thenReturn(List.of(new TaskResponse("id1", "Micronaut", "Estudar", TaskStatus.TODO, now, now)));
        when(taskService.list(eq(0), eq(10), isNull(), eq("Kafka")))
                .thenReturn(List.of(new TaskResponse("id2", "Kafka", "Mensageria", TaskStatus.DONE, now, now)));

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
