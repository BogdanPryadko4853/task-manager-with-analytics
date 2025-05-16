package com.example.taskmanageranalytics.service.impl;

import com.example.taskmanageranalytics.entity.Task;
import com.example.taskmanageranalytics.entity.User;
import com.example.taskmanageranalytics.repository.TaskRepository;
import com.example.taskmanageranalytics.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    @Transactional
    public Task createTask(String taskName, String taskDescription, User author) {
        Task task = Task.builder()
                .title(taskName)
                .description(taskDescription)
                .author(author)
                .build();
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAuthor().equals(currentUser)) {
            throw new RuntimeException("Only task author can delete the task");
        }

        taskRepository.delete(task);
    }

    @Override
    @Transactional(readOnly = true)
    public Task getTask(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Task> getUserTasks(User user) {
        return taskRepository.findByAuthor(user);
    }

    @Override
    @Transactional
    public Task updateTask(Long taskId, String taskName, String taskDescription, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (!task.getAuthor().equals(currentUser)) {
            throw new RuntimeException("Only task author can update the task");
        }

        if (taskName != null) {
            task.setTitle(taskName);
        }
        if (taskDescription != null) {
            task.setDescription(taskDescription);
        }

        return taskRepository.save(task);
    }
}