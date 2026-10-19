package com.example.task_manager.config;

import com.example.task_manager.task.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Order(1)
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final TaskRepository taskRepository;

    public DataSeeder(CategoryRepository categoryRepository, TaskRepository taskRepository) {
        this.categoryRepository = categoryRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(String... args) {
        Category work = categoryRepository.save(new Category("Work"));
        Category home = categoryRepository.save(new Category("Home"));

        save("Learn Spring Boot", "Controllers, services, JPA", TaskStatus.IN_PROGRESS, 7, work);
        save("Write unit tests", null, TaskStatus.TODO, 10, work);
        save("Pay electricity bill", null, TaskStatus.TODO, 3, home);
        save("Clean the kitchen", "Deep clean", TaskStatus.DONE, -1, home);
        save("Plan weekend trip", null, TaskStatus.TODO, 14, null);
    }

    private void save(String title, String desc, TaskStatus status, int daysFromNow, Category category) {
        Task task = new Task(title, desc, status, LocalDate.now().plusDays(daysFromNow));
        task.setCategory(category);
        taskRepository.save(task);
    }
}
