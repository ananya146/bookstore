package com.bookstore.backend.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "users")
public class User {

    // Unique ID for every user
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Customer's full name
    @Column(nullable = false)
    private String name;

    // Email will be used for login
    @Column(nullable = false, unique = true)
    private String email;

    // Password for our MVP login
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    // Points the customer can redeem during payment
    private Integer giftPoints = 0;

    // Empty constructor required by JPA
    public User() {
    }

    // Constructor useful when creating a new user
    public User(String name, String email, String password, Integer giftPoints) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.giftPoints = giftPoints;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getGiftPoints() {
        return giftPoints;
    }

    public void setGiftPoints(Integer giftPoints) {
        this.giftPoints = giftPoints;
    }
}