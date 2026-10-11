package com.bookstore.backend.service;

import com.bookstore.backend.entity.User;
import com.bookstore.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Register a new user
    public User register(User user) {

        // Don't allow two accounts with the same email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        // New users start with zero gift points
        user.setGiftPoints(0);

        return userRepository.save(user);
    }

    // Login using email and password
    public User login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Check the password entered during login
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }
}
