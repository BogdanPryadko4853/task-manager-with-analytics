package com.example.taskmanageranalytics.dto.task;

import lombok.Data;

@Data
public class TaskUpdateRequest {
    private String title;
    private String description;
}
