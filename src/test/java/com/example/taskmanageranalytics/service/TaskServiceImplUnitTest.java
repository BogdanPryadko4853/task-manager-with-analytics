package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.Task;
import com.example.taskmanageranalytics.entity.User;
import com.example.taskmanageranalytics.repository.TaskRepository;
import com.example.taskmanageranalytics.service.impl.TaskServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplUnitTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    private final User testUser = User.builder()
            .id(1L)
            .username("testuser")
            .build();

    private final Task testTask = Task.builder()
            .id(1L)
            .title("Test Task")
            .description("Test Description")
            .author(testUser)
            .build();

    @Test
    void createTask_ShouldReturnSavedTask() {
        // Arrange
        when(taskRepository.save(any(Task.class))).thenReturn(testTask);

        // Act
        Task result = taskService.createTask("Test Task", "Test Description", testUser);

        // Assert
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals(testUser, result.getAuthor());
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deleteTask_ShouldDeleteTask_WhenUserIsAuthor() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        taskService.deleteTask(1L, testUser);

        // Assert
        verify(taskRepository, times(1)).delete(testTask);
    }

    @Test
    void deleteTask_ShouldThrowException_WhenTaskNotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L, testUser));
        verify(taskRepository, never()).delete(any());
    }

    @Test
    void deleteTask_ShouldThrowException_WhenUserNotAuthor() {
        // Arrange
        User otherUser = User.builder().id(2L).username("other").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.deleteTask(1L, otherUser));
        verify(taskRepository, never()).delete(any());
    }

    @Test
    void getTask_ShouldReturnTask_WhenExists() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act
        Task result = taskService.getTask(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTask_ShouldThrowException_WhenNotFound() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> taskService.getTask(1L));
    }

    @Test
    void getAllTasks_ShouldReturnAllTasks() {
        // Arrange
        Task task2 = Task.builder().id(2L).title("Task 2").build();
        when(taskRepository.findAll()).thenReturn(List.of(testTask, task2));

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getUserTasks_ShouldReturnUserTasks() {
        // Arrange
        when(taskRepository.findByAuthor(testUser)).thenReturn(List.of(testTask));

        // Act
        List<Task> result = taskService.getUserTasks(testUser);

        // Assert
        assertEquals(1, result.size());
        assertEquals(testTask, result.get(0));
        verify(taskRepository, times(1)).findByAuthor(testUser);
    }

    @Test
    void updateTask_ShouldUpdateTask_WhenUserIsAuthor() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, "Updated Title", "Updated Desc", testUser);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
        verify(taskRepository, times(1)).save(testTask);
    }

    @Test
    void updateTask_ShouldThrowException_WhenUserNotAuthor() {
        // Arrange
        User otherUser = User.builder().id(2L).username("other").build();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));

        // Act & Assert
        assertThrows(RuntimeException.class,
                () -> taskService.updateTask(1L, "Updated", "Desc", otherUser));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateTask_ShouldUpdateOnlyTitle_WhenDescriptionIsNull() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, "Updated Title", null, testUser);

        // Assert
        assertEquals("Updated Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
    }

    @Test
    void updateTask_ShouldUpdateOnlyDescription_WhenTitleIsNull() {
        // Arrange
        when(taskRepository.findById(1L)).thenReturn(Optional.of(testTask));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Task result = taskService.updateTask(1L, null, "Updated Desc", testUser);

        // Assert
        assertEquals("Test Task", result.getTitle());
        assertEquals("Updated Desc", result.getDescription());
    }
}