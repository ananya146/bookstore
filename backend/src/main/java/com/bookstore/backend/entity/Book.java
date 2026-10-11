package com.bookstore.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book {

    // Unique ID for every book
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Book title shown in the catalogue
    @Column(nullable = false)
    private String title;

    // Author of the book
    @Column(nullable = false)
    private String author;

    // Price of the book
    @Column(nullable = false)
    private Double price;

    // Number of copies currently available
    @Column(nullable = false)
    private Integer stock;

    // Image URL used by the Angular frontend
    private String imageUrl;

    // Short description shown on the product page
    @Column(length = 1000)
    private String description;

    // Each book belongs to one category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Each book belongs to one brand
    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    // Empty constructor required by JPA
    public Book() {
    }

    public Book(String title, String author, Double price, Integer stock,
                String imageUrl, String description,
                Category category, Brand brand) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}