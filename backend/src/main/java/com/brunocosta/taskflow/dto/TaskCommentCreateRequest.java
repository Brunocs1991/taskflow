package com.brunocosta.taskflow.dto;

import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Serdeable
public record TaskCommentCreateRequest(
        @Schema(example = "Criar backend com MongoDB")
        @NotBlank String text
    ) {}
