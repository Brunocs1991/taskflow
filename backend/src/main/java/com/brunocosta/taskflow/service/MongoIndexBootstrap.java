package com.brunocosta.taskflow.service;

import com.brunocosta.taskflow.repository.TaskCommentRepository;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

@Singleton
public class MongoIndexBootstrap {

    private final TaskCommentRepository taskCommentRepository;

    public MongoIndexBootstrap(TaskCommentRepository taskCommentRepository) {
        this.taskCommentRepository = taskCommentRepository;
    }

    @EventListener
    public void onStartup(StartupEvent event) {
        taskCommentRepository.ensureIndexes();
    }
}
