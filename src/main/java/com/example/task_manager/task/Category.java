package com.example.task_manager.task;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    protected Category() { }                 // required by JPA

    public Category(String name) { this.name = name; }

    public Long getId() { return id; }
    public String getName() { return name; }
}
