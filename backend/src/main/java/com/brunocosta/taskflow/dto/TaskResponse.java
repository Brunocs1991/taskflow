package com.brunocosta.taskflow.dto;

import java.time.Instant;

import com.brunocosta.taskflow.domain.TaskStatus;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TaskResponse(
    String id,
    String title,
    String description,
    TaskStatus status,
    Instant createdAt,
    Instant updatedAt
) {}
