package com.brunocosta.taskflow.dto;

import com.brunocosta.taskflow.domain.TaskStatus;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Serdeable
public record TaskCreateRequest(
    @Schema(example = "Criar backend com MongoDB")
    @NotBlank String title,
    String description,
    @NotNull TaskStatus status
){}

