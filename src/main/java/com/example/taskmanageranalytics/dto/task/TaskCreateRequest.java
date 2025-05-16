package com.example.taskmanageranalytics.dto.task;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskCreateRequest {
    @NotBlank
    private String title;
    private String description;
}