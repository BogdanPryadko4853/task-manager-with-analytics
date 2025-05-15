package com.example.taskmanageranalytics.repository;

import com.example.taskmanageranalytics.entity.TaskStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskStatusHistoryRepository extends JpaRepository<TaskStatusHistory, Long> {
    List<TaskStatusHistory> findByTaskIdOrderByChangedAtDesc(Long taskId);
}
