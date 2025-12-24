package com.brunocosta.taskflow.service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;

import com.brunocosta.taskflow.domain.Task;
import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.dto.TaskResponse;
import com.brunocosta.taskflow.error.NotFoundException;
import com.brunocosta.taskflow.repository.TaskRepository;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

@Singleton
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public TaskResponse create(TaskCreateRequest req){
        var task = new Task();
        task.setTitle(req.title());
        task.setDescription(req.description());
        task.setStatus(Objects.isNull(req.status()) ? req.status() : TaskStatus.TODO);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());
        repository.insert(task);
        return toResponse(task);
    }

     public TaskResponse getById(String id) {
        ObjectId oid = parseObjectId(id);
        Task t = repository.findById(oid)
                .orElseThrow(() -> new NotFoundException("Task não encontrada: " + id));
        return toResponse(t);
    }

    public TaskResponse update(String id, TaskCreateRequest req) {
        ObjectId oid = parseObjectId(id);

        Task existing = repository.findById(oid)
                .orElseThrow(() -> new NotFoundException("Task não encontrada: " + id));

        existing.setTitle(req.title());
        existing.setDescription(req.description());
        existing.setStatus(req.status());
        existing.setUpdatedAt(Instant.now());

        repository.update(oid, existing);
        return toResponse(existing);
    }

    public void delete(String id) {
        ObjectId oid = parseObjectId(id);
        boolean deleted = repository.delete(oid);
        if (!deleted) {
            throw new NotFoundException("Task não encontrada: " + id);
        }
    }

    public List<TaskResponse> list(int page, int size, TaskStatus status, String q) {
        return repository.findAll(page, size, status, q)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private ObjectId parseObjectId(String id) {
        try {
            return new ObjectId(id);
        } catch (Exception e) {
            // vai cair no handler de 400 se você preferir, mas aqui simplificamos:
            throw new IllegalArgumentException("ID inválido: " + id);
        }
    }

    private TaskResponse toResponse(Task t) {
        return new TaskResponse(
                t.getId() != null ? t.getId().toHexString() : null,
                t.getTitle(),
                t.getDescription(),
                t.getStatus(),
                t.getCreatedAt(),
                t.getUpdatedAt()
        );
    }

}
