package com.brunocosta.taskflow.service;

import com.brunocosta.taskflow.domain.Task;
import com.brunocosta.taskflow.domain.TaskStatus;
import com.brunocosta.taskflow.dto.TaskCreateRequest;
import com.brunocosta.taskflow.error.NotFoundException;
import com.brunocosta.taskflow.repository.TaskRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Test
    void create_shouldInsertTask_andReturnResponse() {
        TaskRepository repo = mock(TaskRepository.class);
        TaskService service = new TaskService(repo);

        when(repo.insert(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            t.setId(new ObjectId());
            return t;
        });

        var req = new TaskCreateRequest("t1", "d1", TaskStatus.TODO);
        var res = service.create(req);

        assertNotNull(res.id());
        assertEquals("t1", res.title());
        assertEquals(TaskStatus.TODO, res.status());

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(repo).insert(captor.capture());
        assertEquals("t1", captor.getValue().getTitle());
    }

    @Test
    void getById_shouldThrowNotFound_whenMissing() {
        TaskRepository repo = mock(TaskRepository.class);
        TaskService service = new TaskService(repo);

        ObjectId id = new ObjectId();
        when(repo.findById(id)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getById(id.toHexString()));
    }
}
