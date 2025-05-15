package com.example.taskmanageranalytics.service.impl;

import com.example.taskmanageranalytics.entity.TaskStatusHistory;

import java.util.List;

public interface TaskStatusHistoryRepository {

    TaskStatusHistory saveTaskStatusHistory(TaskStatusHistory taskStatusHistory);

    void deleteTaskStatusHistory(TaskStatusHistory taskStatusHistory);

    TaskStatusHistory getTaskStatusHistoryById(long id);

    List<TaskStatusHistory> getAllTaskStatusHistory();

}
