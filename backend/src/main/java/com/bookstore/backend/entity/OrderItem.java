package com.bookstore.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    // Unique ID for this order item
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order this item belongs to
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Book that was purchased
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Number of copies purchased
    @Column(nullable = false)
    private Integer quantity;

    // Price of the book when the order was placed
    @Column(nullable = false)
    private Double priceAtPurchase;

    // Empty constructor required by JPA
    public OrderItem() {
    }

    public OrderItem(Order order, Book book, Integer quantity,
                     Double priceAtPurchase) {
        this.order = order;
        this.book = book;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(Double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}