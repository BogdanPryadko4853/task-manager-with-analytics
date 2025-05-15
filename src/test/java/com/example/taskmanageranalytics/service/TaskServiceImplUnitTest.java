package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.Task;
import com.example.taskmanageranalytics.repository.TaskRepository;
import com.example.taskmanageranalytics.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    public void shouldCreateTaskTest() {
        String taskName = "taskName";
        String taskDescription = "taskDescription";

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task savedTask = invocation.getArgument(0);
            return savedTask;
        });

        Task createdTask = taskService.saveTask(taskName, taskDescription);

        assertNotNull(createdTask);
        assertEquals(taskName, createdTask.getTitle());
        assertEquals(taskDescription, createdTask.getDescription());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void shouldUpdateTaskTest() {
        Long taskId = 1L;
        String newTitle = "Новое название";
        String newDescription = "Новое описание";

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Старое название");
        existingTask.setDescription("Старое описание");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.updateTask(taskId, newTitle, newDescription);

        assertEquals(newTitle, existingTask.getTitle());
        assertEquals(newDescription, existingTask.getDescription());

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    public void shouldThrowExceptionWhenTaskNotFound() {
        Long taskId = 999L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            taskService.updateTask(taskId, "Название", "Описание");
        });

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).save(any());
    }

    @Test
    public void shouldUpdateOnlyTitleWhenDescriptionIsNull() {
        Long taskId = 1L;
        String newTitle = "Только название";

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Старое название");
        existingTask.setDescription("Описание");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.updateTask(taskId, newTitle, null);

        assertEquals(newTitle, existingTask.getTitle());
        assertEquals("Описание", existingTask.getDescription()); // Описание не изменилось
    }

    @Test
    public void shouldUpdateOnlyDescriptionWhenTitleIsNull() {
        Long taskId = 1L;
        String newDescription = "Только описание";

        Task existingTask = new Task();
        existingTask.setId(taskId);
        existingTask.setTitle("Название");
        existingTask.setDescription("Старое описание");

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        taskService.updateTask(taskId, null, newDescription);

        assertEquals("Название", existingTask.getTitle()); // Название не изменилось
        assertEquals(newDescription, existingTask.getDescription());
    }

    @Test
    public void shouldDeleteExistingTask() {
        Long taskId = 1L;
        Task existingTask = new Task();
        existingTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskRepository).delete(existingTask);

        taskService.deleteTask(taskId);

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    public void shouldNotThrowExceptionWhenTaskNotFoundForDeletion() {
        Long taskId = 999L;

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> taskService.deleteTask(taskId));

        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, never()).delete(any());
    }

    @Test
    public void shouldReturnTaskWhenExists() {
        Long taskId = 1L;
        Task expectedTask = new Task();
        expectedTask.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(expectedTask));

        Task result = taskService.getTask(taskId);

        assertEquals(expectedTask, result);
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void shouldReturnAllTasks() {
        List<Task> expectedTasks = new ArrayList<>();
        Task task1 = Task.builder()
                .title("First Task")
                .description("First Task")
                .build();
        Task task2 = Task.builder()
                .title("Second Task")
                .description("Second Task")
                .build();
        expectedTasks.add(task1);
        expectedTasks.add(task2);

        when(taskRepository.findAll()).thenReturn(expectedTasks);

        List<Task> result = taskService.getAllTasks();

        assertEquals(expectedTasks.size(), result.size());
        assertEquals(expectedTasks, result);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void shouldReturnEmptyListWhenNoTasks() {
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        List<Task> result = taskService.getAllTasks();

        assertTrue(result.isEmpty());
        verify(taskRepository, times(1)).findAll();
    }


}
