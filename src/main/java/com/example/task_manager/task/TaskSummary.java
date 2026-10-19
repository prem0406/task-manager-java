package com.example.task_manager.task;

import java.time.LocalDate;

public interface TaskSummary {
    Long getId();
    String getTitle();
    TaskStatus getStatus();
    LocalDate getDueDate();
}
