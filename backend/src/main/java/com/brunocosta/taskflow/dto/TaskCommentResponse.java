package com.brunocosta.taskflow.dto;

import java.time.Instant;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public record TaskCommentResponse(
                String id,
                String taskId,
                String text,
                Instant createdAt) {

}
