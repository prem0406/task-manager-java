package com.example.task_manager.service;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.task.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CategoryRepository categoryRepository;

    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    public TaskResponse getById(Long id) {
        return TaskResponse.from(findEntity(id));
    }

    @Transactional
    public TaskResponse create(TaskRequest request) {
        TaskStatus status = request.status() != null ? request.status() : TaskStatus.TODO;
        Task task = new Task(request.title(), request.description(), status, request.dueDate());
        task.setCategory(resolveCategory(request.categoryId()));
        return TaskResponse.from(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findEntity(id);
        task.update(request.title(), request.description(), request.status(), request.dueDate());
        task.setCategory(resolveCategory(request.categoryId()));
        return TaskResponse.from(task);        // no save() call needed, see below
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.delete(findEntity(id));
    }

    private Task findEntity(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found: " + id));
    }

    private Category resolveCategory(Long categoryId) {
        if (categoryId == null) return null;
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));
    }
}
