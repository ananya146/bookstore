
package com.bookstore.backend.service;

import com.bookstore.backend.entity.Order;
import com.bookstore.backend.entity.Payment;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.repository.OrderRepository;
import com.bookstore.backend.repository.PaymentRepository;
import com.bookstore.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Payment processPayment(Long orderId, String paymentMethod) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException("Payment already exists");
        }

        if ("CANCELLED".equalsIgnoreCase(order.getStatus())) {
            throw new RuntimeException(
                    "Payment cannot be processed for a cancelled order");
        }

        Payment payment = new Payment(
                order,
                paymentMethod,
                order.getTotalAmount(),
                "SUCCESS",
                LocalDateTime.now()
        );

        Payment savedPayment = paymentRepository.save(payment);

        // Award points only once, after the payment record is saved.
        User user = order.getUser();

        int currentPoints = user.getGiftPoints() == null
                ? 0
                : user.getGiftPoints();

        int earnedPoints = (int) Math.floor(
                Math.max(0.0, order.getTotalAmount()) * 0.05
        );

        user.setGiftPoints(currentPoints + earnedPoints);
        userRepository.save(user);

        return savedPayment;
    }
}
