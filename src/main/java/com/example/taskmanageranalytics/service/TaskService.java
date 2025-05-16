package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.Task;
import com.example.taskmanageranalytics.entity.User;

import java.util.List;

public interface TaskService {
    Task createTask(String taskName, String taskDescription, User author);

    void deleteTask(Long taskId, User currentUser);

    Task getTask(Long taskId);

    List<Task> getAllTasks();

    List<Task> getUserTasks(User user);

    Task updateTask(Long taskId, String taskName, String taskDescription, User currentUser);
}