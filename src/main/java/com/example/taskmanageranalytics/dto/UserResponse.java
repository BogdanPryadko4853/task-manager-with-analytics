package com.example.taskmanageranalytics.dto;

import com.example.taskmanageranalytics.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private User.Role role;
    private LocalDateTime createdAt;
    private Integer createdTasksCount;
    private Integer assignedTasksCount;
}