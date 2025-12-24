package com.brunocosta.taskflow.domain;

import java.time.Instant;

import org.bson.types.ObjectId;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Task {

    private ObjectId id;
    private String title;
    private String description;
    private TaskStatus status;
    private Instant createdAt;
    private Instant updatedAt;

}
