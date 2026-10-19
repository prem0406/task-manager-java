package com.example.task_manager;

import com.example.task_manager.dto.TaskRequest;
import com.example.task_manager.dto.TaskResponse;
import com.example.task_manager.service.TaskService;
import com.example.task_manager.task.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskRepository taskRepository;

    public TaskController(TaskService taskService, TaskRepository taskRepository) {
        this.taskService = taskService;
        this.taskRepository = taskRepository;
    }

    // GET /api/tasks?status=TODO&page=0&size=5&sort=dueDate,desc
    @GetMapping
    @Operation(summary = "List tasks", description = "Returns a paginated summary of tasks with the selected status.")
    public PagedModel<TaskSummary> list(
            @Parameter(description = "Task status to filter by")
            @RequestParam(defaultValue = "TODO") TaskStatus status,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return new PagedModel<>(taskRepository.findSummariesByStatus(status, pageable));
    }

    // GET /api/tasks/views?status=TODO  (flattened projection)
    @GetMapping("/views")
    @Operation(summary = "List task views", description = "Returns task summaries with a flattened category name.")
    public List<TaskView> views(
            @Parameter(description = "Task status to filter by")
            @RequestParam(defaultValue = "TODO") TaskStatus status) {
        return taskRepository.findViewsByStatus(status);
    }

    // GET /api/tasks/stats
    @GetMapping("/stats")
    @Operation(summary = "Get task status counts")
    public List<StatusCount> stats() {
        return taskRepository.countByStatus();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a task by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task found"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public TaskResponse get(@Parameter(description = "Task ID") @PathVariable Long id) {
        return taskService.getById(id);
    }

    @PostMapping
    @Operation(summary = "Create a task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Task created"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<TaskResponse> create(@Valid @RequestBody TaskRequest request) {
        TaskResponse created = taskService.create(request);
        return ResponseEntity.created(URI.create("/api/tasks/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Task updated"),
            @ApiResponse(responseCode = "400", description = "Invalid task data"),
            @ApiResponse(responseCode = "404", description = "Task or category not found")
    })
    public TaskResponse update(@Parameter(description = "Task ID") @PathVariable Long id,
                               @Valid @RequestBody TaskRequest request) {
        return taskService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Task deleted"),
            @ApiResponse(responseCode = "404", description = "Task not found")
    })
    public void delete(@Parameter(description = "Task ID") @PathVariable Long id) {
        taskService.delete(id);
    }
}
