package com.bookstore.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    // Unique ID for the order
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who placed the order
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Address selected for this order
    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    // Current order status
    // Example: CONFIRMED, CANCELLED, DELIVERED
    @Column(nullable = false)
    private String status;

    // Total amount of the order
    @Column(nullable = false)
    private Double totalAmount;

    // When the order was placed
    @Column(nullable = false)
    private LocalDateTime orderDate;

    // Tentative delivery date shown to the customer
    private LocalDateTime tentativeDeliveryDate;

    // When the order was cancelled, if applicable
    private LocalDateTime cancelledAt;

    // Gift points used during this purchase
    private Integer giftPointsUsed = 0;

    // Empty constructor required by JPA
    public Order() {
    }

    public Order(User user, Address address, String status,
                 Double totalAmount, LocalDateTime orderDate,
                 LocalDateTime tentativeDeliveryDate,
                 Integer giftPointsUsed) {
        this.user = user;
        this.address = address;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
        this.tentativeDeliveryDate = tentativeDeliveryDate;
        this.giftPointsUsed = giftPointsUsed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public LocalDateTime getTentativeDeliveryDate() {
        return tentativeDeliveryDate;
    }

    public void setTentativeDeliveryDate(LocalDateTime tentativeDeliveryDate) {
        this.tentativeDeliveryDate = tentativeDeliveryDate;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public Integer getGiftPointsUsed() {
        return giftPointsUsed;
    }

    public void setGiftPointsUsed(Integer giftPointsUsed) {
        this.giftPointsUsed = giftPointsUsed;
    }
}