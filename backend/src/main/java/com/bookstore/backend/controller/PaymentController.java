package com.bookstore.backend.controller;

import com.bookstore.backend.entity.Payment;
import com.bookstore.backend.service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public Payment processPayment(
            @RequestParam Long orderId,
            @RequestParam String paymentMethod) {

        return paymentService.processPayment(orderId, paymentMethod);
    }
}
