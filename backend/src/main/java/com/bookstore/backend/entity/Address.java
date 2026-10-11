package com.bookstore.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

    // Unique ID for the address
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who owns this address
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Name of the person receiving the order
    @Column(nullable = false)
    private String fullName;

    // House number, street, etc.
    @Column(nullable = false)
    private String addressLine;

    // City
    @Column(nullable = false)
    private String city;

    // State
    @Column(nullable = false)
    private String state;

    // Postal/PIN code
    @Column(nullable = false)
    private String postalCode;

    // Phone number for delivery
    @Column(nullable = false)
    private String phone;

    // Empty constructor required by JPA
    public Address() {
    }

    public Address(User user, String fullName, String addressLine,
                   String city, String state, String postalCode,
                   String phone) {
        this.user = user;
        this.fullName = fullName;
        this.addressLine = addressLine;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.phone = phone;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddressLine() {
        return addressLine;
    }

    public void setAddressLine(String addressLine) {
        this.addressLine = addressLine;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}