package com.brunocosta.taskflow.service;

import com.brunocosta.taskflow.repository.TaskCommentRepository;

import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class MongoIndexBootstrap {

    private final TaskCommentRepository repository;

    @EventListener
    public void onStartup(StartupEvent event) {
        repository.ensureIndexes();
    }

}
