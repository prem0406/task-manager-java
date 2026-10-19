package com.example.task_manager.dto;

import com.example.task_manager.task.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TaskRequest(
        @NotBlank @Size(max = 100) String title,
        @Size(max = 500) String description,
        TaskStatus status,
        @FutureOrPresent LocalDate dueDate,
        Long categoryId) { }
