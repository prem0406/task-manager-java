package com.example.task_manager.task;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByTitleContainingIgnoreCase(String text);

    List<Task> findByStatusAndDueDateBefore(TaskStatus status, LocalDate date);

    List<Task> findByCategoryName(String categoryName);   // follows task.category.name

    // Interface projection (derived query)
    List<TaskSummary> findSummariesByStatus(TaskStatus status);

    // Record projection (derived query)
    List<TaskBrief> findBriefByStatus(TaskStatus status);

    // Dynamic projection: caller chooses the type
    <T> List<T> findByStatus(TaskStatus status, Class<T> type);

    // JPQL with constructor expression, flattening the category name
    @Query("""
        select new com.example.task_manager.task.TaskView(t.id, t.title, t.status, c.name)
        from Task t
        left join t.category c
        where t.status = :status
        """)
    List<TaskView> findViewsByStatus(@Param("status") TaskStatus status);

    // Aggregate query returning an interface projection (aliases must match getter names)
    @Query("select t.status as status, count(t) as total from Task t group by t.status")
    List<StatusCount> countByStatus();

    Page<TaskSummary> findSummariesByStatus(TaskStatus status, Pageable pageable);
}



