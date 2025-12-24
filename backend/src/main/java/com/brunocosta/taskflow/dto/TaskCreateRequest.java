package com.brunocosta.taskflow.dto;

import com.brunocosta.taskflow.domain.TaskStatus;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record TaskCreateRequest(
    @NotBlank String title,
    String description,
    @NotNull TaskStatus status
){}

