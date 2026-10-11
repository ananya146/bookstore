package com.bookstore.backend.controller;

import com.bookstore.backend.entity.CartItem;
import com.bookstore.backend.entity.Order;
import com.bookstore.backend.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "http://localhost:4200")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestParam Long userId,
            @RequestParam Long addressId,
            @RequestParam String paymentMethod,
            @RequestParam(required = false) Integer giftPointsUsed) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(
                        userId,
                        addressId,
                        paymentMethod,
                        giftPointsUsed
                ));
    }

    @GetMapping("/user/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getUserOrders(userId);
    }

    @PostMapping("/{orderId}/cancel")
    public Order cancelOrder(@PathVariable Long orderId) {
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/{orderId}/buy-again")
    public List<CartItem> buyAgain(@PathVariable Long orderId) {
        return orderService.buyAgain(orderId);
    }
}
