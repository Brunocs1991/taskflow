package com.brunocosta.taskflow.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;

import com.brunocosta.taskflow.domain.TaskComment;
import com.brunocosta.taskflow.dto.TaskCommentCreateRequest;
import com.brunocosta.taskflow.dto.TaskCommentResponse;
import com.brunocosta.taskflow.error.NotFoundException;
import com.brunocosta.taskflow.repository.TaskCommentRepository;
import com.brunocosta.taskflow.repository.TaskRepository;

import jakarta.inject.Singleton;

@Singleton
public class TaskCommentService {

    private final TaskRepository taskRepository;
    private final TaskCommentRepository taskCommentRepository;

    

    public TaskCommentService(TaskRepository taskRepository, TaskCommentRepository taskCommentRepository) {
        this.taskRepository = taskRepository;
        this.taskCommentRepository = taskCommentRepository;
    }

    public TaskCommentResponse addComment(String taskId, TaskCommentCreateRequest request) {
        ObjectId taskOid = parseObjectId(taskId);
        taskRepository.findById(taskOid).orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
        TaskComment c = new TaskComment();
        c.setTaskId(taskOid);
        c.setText(request.text());
        c.setCreatedAt(Instant.now());
        taskCommentRepository.insert(c);
        return toResponse(c);
    }

    public List<TaskCommentResponse> listComments(String taskId) {
        ObjectId taskOid = parseObjectId(taskId);

        taskRepository.findById(taskOid).orElseThrow(() -> new NotFoundException("Task not found: " + taskId));
        return taskCommentRepository.findByTaskId(taskOid)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ObjectId parseObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid Id: " + id);
        }
    }

    private TaskCommentResponse toResponse(TaskComment c) {
        return new TaskCommentResponse(
                Objects.nonNull(c.getId()) ? c.getId().toHexString() : null,
                Objects.nonNull(c.getTaskId()) ? c.getTaskId().toHexString() : null,
                c.getText(),
                c.getCreatedAt());
    }

}
