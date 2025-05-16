package com.example.taskmanageranalytics.repository;

import com.example.taskmanageranalytics.entity.Task;
import com.example.taskmanageranalytics.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAuthor(User author);
}
