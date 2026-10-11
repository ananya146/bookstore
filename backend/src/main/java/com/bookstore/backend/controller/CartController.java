package com.bookstore.backend.controller;

import com.bookstore.backend.entity.CartItem;
import com.bookstore.backend.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "http://localhost:4200")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/user/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/items")
    public CartItem addToCart(
            @RequestParam Long userId,
            @RequestParam Long bookId,
            @RequestParam Integer quantity) {

        return cartService.addToCart(userId, bookId, quantity);
    }

    @PutMapping("/items/{id}")
    public CartItem updateQuantity(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        return cartService.updateQuantity(id, quantity);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long id) {

        cartService.removeFromCart(id);

        return ResponseEntity.noContent().build();
    }
}
