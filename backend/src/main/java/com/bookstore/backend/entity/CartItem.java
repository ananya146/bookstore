package com.bookstore.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {

    // Unique ID for this cart item
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The cart this item belongs to
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // The book being added to the cart
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    // Number of copies of this book
    @Column(nullable = false)
    private Integer quantity;

    // Empty constructor required by JPA
    public CartItem() {
    }

    public CartItem(Cart cart, Book book, Integer quantity) {
        this.cart = cart;
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
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
}
