package com.example.taskmanageranalytics.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskStatusHistoryRepository extends JpaRepository<TaskStatusHistoryRepository, Long> {
}
