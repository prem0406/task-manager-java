package com.example.task_manager.dto;

import com.example.task_manager.task.Task;
import com.example.task_manager.task.TaskStatus;

import java.time.Instant;
import java.time.LocalDate;

public record TaskResponse(Long id, String title, String description, TaskStatus status,
                           LocalDate dueDate, Instant createdAt, String categoryName) {

    public static TaskResponse from(Task t) {
        return new TaskResponse(t.getId(), t.getTitle(), t.getDescription(), t.getStatus(),
                t.getDueDate(), t.getCreatedAt(),
                t.getCategory() != null ? t.getCategory().getName() : null);
    }
}
