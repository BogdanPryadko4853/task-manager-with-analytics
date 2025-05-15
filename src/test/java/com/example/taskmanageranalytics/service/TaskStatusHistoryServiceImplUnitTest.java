package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.TaskStatusHistory;
import com.example.taskmanageranalytics.repository.TaskStatusHistoryRepository;
import com.example.taskmanageranalytics.service.impl.TaskStatusHistoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskStatusHistoryServiceImplUnitTest {

    @Mock
    private TaskStatusHistoryRepository taskStatusHistoryRepository;

    @InjectMocks
    private TaskStatusHistoryServiceImpl taskStatusHistoryService;

    @Test
    void saveTaskStatusHistory_ShouldReturnSavedHistory() {
        TaskStatusHistory history = new TaskStatusHistory();
        when(taskStatusHistoryRepository.save(history)).thenReturn(history);

        TaskStatusHistory result = taskStatusHistoryService.saveTaskStatusHistory(history);

        assertNotNull(result);
        verify(taskStatusHistoryRepository).save(history);
    }

    @Test
    void deleteTaskStatusHistory_ShouldCallRepositoryDelete() {
        Long id = 1L;
        doNothing().when(taskStatusHistoryRepository).deleteById(id);

        taskStatusHistoryService.deleteTaskStatusHistory(id);

        verify(taskStatusHistoryRepository).deleteById(id);
    }

    @Test
    void getTaskStatusHistoryById_ShouldReturnHistoryWhenExists() {
        Long id = 1L;
        TaskStatusHistory expected = new TaskStatusHistory();
        when(taskStatusHistoryRepository.findById(id)).thenReturn(Optional.of(expected));

        TaskStatusHistory result = taskStatusHistoryService.getTaskStatusHistoryById(id);

        assertEquals(expected, result);
        verify(taskStatusHistoryRepository).findById(id);
    }

    @Test
    void getTaskStatusHistoryById_ShouldThrowExceptionWhenNotFound() {
        Long id = 999L;
        when(taskStatusHistoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                taskStatusHistoryService.getTaskStatusHistoryById(id));
        verify(taskStatusHistoryRepository).findById(id);
    }

    @Test
    void getAllTaskStatusHistory_ShouldReturnAllHistories() {
        List<TaskStatusHistory> expected = List.of(
                createTestHistory(1L),
                createTestHistory(2L)
        );
        when(taskStatusHistoryRepository.findAll()).thenReturn(expected);

        List<TaskStatusHistory> result = taskStatusHistoryService.getAllTaskStatusHistory();

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(taskStatusHistoryRepository).findAll();
    }

    @Test
    void getAllTaskStatusHistory_ShouldReturnEmptyListWhenNoData() {
        when(taskStatusHistoryRepository.findAll()).thenReturn(Collections.emptyList());

        List<TaskStatusHistory> result = taskStatusHistoryService.getAllTaskStatusHistory();

        assertTrue(result.isEmpty());
        verify(taskStatusHistoryRepository).findAll();
    }

    @Test
    void getHistoryByTaskId_ShouldReturnOrderedList() {
        Long taskId = 1L;
        List<TaskStatusHistory> expected = List.of(
                createTestHistory(1L, LocalDateTime.now().minusDays(1)),
                createTestHistory(2L, LocalDateTime.now())
        );
        when(taskStatusHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId)).thenReturn(expected);

        List<TaskStatusHistory> result = taskStatusHistoryService.getHistoryByTaskId(taskId);

        assertEquals(2, result.size());
        assertEquals(expected, result);
        verify(taskStatusHistoryRepository).findByTaskIdOrderByChangedAtDesc(taskId);
    }

    @Test
    void getHistoryByTaskId_ShouldReturnEmptyListWhenNoData() {
        Long taskId = 999L;
        when(taskStatusHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId))
                .thenReturn(Collections.emptyList());

        List<TaskStatusHistory> result = taskStatusHistoryService.getHistoryByTaskId(taskId);

        assertTrue(result.isEmpty());
        verify(taskStatusHistoryRepository).findByTaskIdOrderByChangedAtDesc(taskId);
    }

    private TaskStatusHistory createTestHistory(Long id) {
        TaskStatusHistory history = new TaskStatusHistory();
        history.setId(id);
        return history;
    }

    private TaskStatusHistory createTestHistory(Long id, LocalDateTime changedAt) {
        TaskStatusHistory history = createTestHistory(id);
        history.setChangedAt(changedAt);
        return history;
    }
}