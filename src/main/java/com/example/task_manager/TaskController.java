package com.example.task_manager;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.task.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    // GET /api/tasks?status=TODO&page=0&size=5&sort=dueDate,desc
    @GetMapping
    public PagedModel<TaskSummary> list(
            @RequestParam(defaultValue = "TODO") TaskStatus status,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return new PagedModel<>(taskRepository.findSummariesByStatus(status, pageable));
    }

    // GET /api/tasks/views?status=TODO  (flattened projection)
    @GetMapping("/views")
    public List<TaskView> views(@RequestParam(defaultValue = "TODO") TaskStatus status) {
        return taskRepository.findViewsByStatus(status);
    }

    // GET /api/tasks/stats
    @GetMapping("/stats")
    public List<StatusCount> stats() {
        return taskRepository.countByStatus();
    }

    @GetMapping("/{id}")
    public TaskResponse get(@PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse created = taskService.create(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
