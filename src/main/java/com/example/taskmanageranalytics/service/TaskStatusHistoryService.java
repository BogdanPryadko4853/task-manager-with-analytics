package com.example.taskmanageranalytics.service;

import com.example.taskmanageranalytics.entity.TaskStatusHistory;

import java.util.List;

public interface TaskStatusHistoryService {
    TaskStatusHistory saveTaskStatusHistory(TaskStatusHistory taskStatusHistory);

    void deleteTaskStatusHistory(Long id);

    TaskStatusHistory getTaskStatusHistoryById(long id);

    List<TaskStatusHistory> getAllTaskStatusHistory();

    List<TaskStatusHistory> getHistoryByTaskId(Long taskId);
}