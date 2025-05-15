package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.Task;

import java.util.List;

public interface TaskService {
    Task saveTask(String taskName, String taskDescription);

    void deleteTask(Long taskId);

    Task getTask(Long taskId);

    List<Task> getAllTasks();

    void updateTask(Long taskId, String taskName, String taskDescription);
}
