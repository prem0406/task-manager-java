package com.example.task_manager.task;

public record TaskView(Long id, String title, TaskStatus status, String categoryName) { }
