package com.example.task_manager.service;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.exception.ResourceNotFoundException;
import com.example.task_manager.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

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
        TaskResponse response = TaskResponse.from(taskRepository.save(task));
        logger.info("Created task id={}", response.id());
        return response;
    }

    @Transactional
    public TaskResponse update(Long id, TaskRequest request) {
        Task task = findEntity(id);
        task.update(request.title(), request.description(), request.status(), request.dueDate());
        task.setCategory(resolveCategory(request.categoryId()));
        TaskResponse response = TaskResponse.from(task);        // no save() call needed, see below
        logger.info("Updated task id={}", response.id());
        return response;
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.delete(findEntity(id));
        logger.info("Deleted task id={}", id);
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
