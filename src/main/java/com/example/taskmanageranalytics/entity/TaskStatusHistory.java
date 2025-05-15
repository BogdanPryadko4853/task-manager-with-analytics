package com.example.taskmanageranalytics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_status_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskStatusHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus oldStatus;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus newStatus;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime changedAt;

}