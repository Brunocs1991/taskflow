package com.brunocosta.taskflow.domain;

import java.time.Instant;

import org.bson.types.ObjectId;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskComment {
    private ObjectId id;
    private ObjectId taskId;
    private String text;
    private Instant createdAt;

}
