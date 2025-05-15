package com.example.taskmanageranalytics.service.impl;

import com.example.taskmanageranalytics.entity.Task;
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
    public Task saveTask(String taskName, String taskDescription) {
        Task task = Task.builder()
                .title(taskName)
                .description(taskDescription)
                .build();
        return taskRepository.save(task);
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.findById(taskId).ifPresent(taskRepository::delete);
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
    @Transactional
    public void updateTask(Long taskId, String taskName, String taskDescription) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        if (taskName != null) {
            task.setTitle(taskName);
        }
        if (taskDescription != null) {
            task.setDescription(taskDescription);
        }
        taskRepository.save(task);
    }
}
