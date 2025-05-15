package com.example.taskmanageranalytics.service.impl;

import com.example.taskmanageranalytics.entity.TaskStatusHistory;
import com.example.taskmanageranalytics.repository.TaskStatusHistoryRepository;
import com.example.taskmanageranalytics.service.TaskStatusHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskStatusHistoryServiceImpl implements TaskStatusHistoryService {

    private final TaskStatusHistoryRepository taskStatusHistoryRepository;

    @Override
    @Transactional
    public TaskStatusHistory saveTaskStatusHistory(TaskStatusHistory taskStatusHistory) {
        return taskStatusHistoryRepository.save(taskStatusHistory);
    }

    @Override
    @Transactional
    public void deleteTaskStatusHistory(Long id) {
        taskStatusHistoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public TaskStatusHistory getTaskStatusHistoryById(long id) {
        return taskStatusHistoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task status history not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatusHistory> getAllTaskStatusHistory() {
        return taskStatusHistoryRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatusHistory> getHistoryByTaskId(Long taskId) {
        return taskStatusHistoryRepository.findByTaskIdOrderByChangedAtDesc(taskId);
    }
}
