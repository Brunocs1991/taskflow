package com.brunocosta.taskflow.dto;

import io.micronaut.serde.annotation.Serdeable;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record TaskCommentCreateRequest(
        @NotBlank String text
    ) {}
