package com.example.taskmanageranalytics.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private TaskStatus status = TaskStatus.TODO;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TaskStatusHistory> statusHistory = new ArrayList<>();


    public void changeStatus(TaskStatus newStatus, User changedBy) {
        if (this.status != newStatus) {
            TaskStatusHistory history = TaskStatusHistory.builder()
                    .task(this)
                    .oldStatus(this.status)
                    .newStatus(newStatus)
                    .changedBy(changedBy)
                    .build();

            this.statusHistory.add(history);
            this.status = newStatus;

            if (newStatus == TaskStatus.DONE) {
                this.completedAt = LocalDateTime.now();
            }
        }
    }
}