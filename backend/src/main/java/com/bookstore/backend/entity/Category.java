package com.bookstore.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category {

    // Unique ID for each category
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Category name, for example "Fiction" or "Technology"
    @Column(nullable = false, unique = true)
    private String name;

    // Empty constructor required by JPA
    public Category() {
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}