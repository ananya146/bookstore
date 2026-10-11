package com.bookstore.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    // Unique ID for the payment
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Order for which this payment was made
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    // Payment method selected by the customer
    // Example: CARD, UPI, COD
    @Column(nullable = false)
    private String paymentMethod;

    // Amount paid
    @Column(nullable = false)
    private Double amount;

    // Payment status
    // Example: SUCCESS, FAILED
    @Column(nullable = false)
    private String status;

    // When the payment was made
    @Column(nullable = false)
    private LocalDateTime paymentDate;

    // Empty constructor required by JPA
    public Payment() {
    }

    public Payment(Order order, String paymentMethod,
                   Double amount, String status,
                   LocalDateTime paymentDate) {
        this.order = order;
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime paymentDate) {
        this.paymentDate = paymentDate;
    }
}
