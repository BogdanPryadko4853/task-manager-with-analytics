package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.Task;

import java.util.List;

public interface TaskService {
    public void saveTask(String taskName, String taskDescription);

    public void deleteTask(Long taskId);

    public Task getTask(Long taskId);

    public List<Task> getAllTasks();

    public void updateTask(Long taskId, String taskName, String taskDescription);
}
