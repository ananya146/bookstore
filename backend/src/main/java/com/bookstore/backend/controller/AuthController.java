package com.bookstore.backend.controller;

import com.bookstore.backend.dto.LoginRequest;
import com.bookstore.backend.dto.RegisterRequest;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        User registeredUser = authService.register(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of(
                        "id", registeredUser.getId(),
                        "name", registeredUser.getName(),
                        "email", registeredUser.getEmail(),
                        "giftPoints", registeredUser.getGiftPoints()
                ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        User user = authService.login(
                request.getEmail(),
                request.getPassword()
        );

        return ResponseEntity.ok(
                Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "email", user.getEmail(),
                        "giftPoints", user.getGiftPoints()
                )
        );
    }
}