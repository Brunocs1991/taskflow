package com.brunocosta.taskflow;

import static org.mockito.Mockito.mock;

import com.brunocosta.taskflow.service.TaskCommentService;
import com.brunocosta.taskflow.service.TaskService;
import com.mongodb.client.MongoClient;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import jakarta.inject.Singleton;

@Factory
public class TestMocks {

    @Singleton
    @Replaces(TaskService.class)
    TaskService taskService() {
        return mock(TaskService.class);
    }

    @Singleton
    @Replaces(TaskCommentService.class)
    TaskCommentService taskCommentService() {
        return mock(TaskCommentService.class);
    }

    @Singleton
    @Replaces(MongoClient.class)
    MongoClient mongoClient() {
        return mock(MongoClient.class);
    }
}
