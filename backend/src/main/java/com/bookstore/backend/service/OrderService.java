
package com.bookstore.backend.service;

import com.bookstore.backend.entity.*;
import com.bookstore.backend.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final BookRepository bookRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            AddressRepository addressRepository,
            BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.bookRepository = bookRepository;
    }

    // Create an order from the user's current cart.
    @Transactional
    public Order createOrder(
            Long userId,
            Long addressId,
            String paymentMethod,
            Integer giftPointsUsed) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address not found"));

        if (address.getUser() == null
                || !address.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "This address does not belong to the user");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cart not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Cart is empty");
        }

        double total = 0;

        for (CartItem item : cartItems) {
            Book book = item.getBook();

            if (item.getQuantity() <= 0) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid quantity for book: " + book.getTitle());
            }

            if (book.getStock() < item.getQuantity()) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Insufficient stock for book: " + book.getTitle());
            }

            total += book.getPrice() * item.getQuantity();
        }

        int requestedPoints = giftPointsUsed == null ? 0 : giftPointsUsed;

        if (requestedPoints < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Gift points cannot be negative");
        }

        if (requestedPoints > user.getGiftPoints()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "You do not have enough gift points");
        }

        // One gift point gives a one-rupee discount.
        // Never redeem more points than the order subtotal.
        int pointsToRedeem = (int) Math.min(
                requestedPoints,
                Math.floor(total)
        );

        double finalAmount = Math.max(0, total - pointsToRedeem);

        Order order = new Order(
                user,
                address,
                "PLACED",
                finalAmount,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(5),
                pointsToRedeem
        );

        Order savedOrder = orderRepository.save(order);

        for (CartItem item : cartItems) {
            OrderItem orderItem = new OrderItem(
                    savedOrder,
                    item.getBook(),
                    item.getQuantity(),
                    item.getBook().getPrice()
            );

            orderItemRepository.save(orderItem);

            Book book = item.getBook();
            book.setStock(book.getStock() - item.getQuantity());
            bookRepository.save(book);
        }

        // Deduct only the points actually redeemed.
        user.setGiftPoints(user.getGiftPoints() - pointsToRedeem);
        userRepository.save(user);

        // Empty the cart after successfully creating the order.
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;
    }

    // Get a user's previous orders.
    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    // Cancel an order within 48 hours.
    @Transactional
    public Order cancelOrder(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        LocalDateTime deadline = order.getOrderDate().plusHours(48);

        if (LocalDateTime.now().isAfter(deadline)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cancellation window has expired");
        }

        if ("CANCELLED".equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Order is already cancelled");
        }

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem orderItem : orderItems) {
            Book book = orderItem.getBook();
            book.setStock(book.getStock() + orderItem.getQuantity());
            bookRepository.save(book);
        }

        // Return redeemed points when an order is cancelled.
        User user = order.getUser();
        user.setGiftPoints(
                user.getGiftPoints() + order.getGiftPointsUsed());
        userRepository.save(user);

        order.setStatus("CANCELLED");
        order.setCancelledAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    // Put all books from a previous order back into the cart.
    @Transactional
    public List<CartItem> buyAgain(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Order not found"));

        Cart cart = cartRepository.findByUserId(order.getUser().getId())
                .orElseGet(() -> cartRepository.save(
                        new Cart(order.getUser())));

        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem orderItem : orderItems) {
            CartItem cartItem = new CartItem(
                    cart,
                    orderItem.getBook(),
                    orderItem.getQuantity());

            cartItemRepository.save(cartItem);
        }

        return cartItemRepository.findByCartId(cart.getId());
    }

    // Recommend books from the category of the latest purchase.
    public List<Book> getRecommendations(Long userId) {

        List<Order> orders = orderRepository.findByUserId(userId);

        if (orders.isEmpty()) {
            return List.of();
        }

        Order latestOrder = orders.get(orders.size() - 1);

        List<OrderItem> items =
                orderItemRepository.findByOrderId(latestOrder.getId());

        if (items.isEmpty()) {
            return List.of();
        }

        Long categoryId = items.get(0)
                .getBook()
                .getCategory()
                .getId();

        return bookRepository.findByCategoryId(categoryId);
    }
}
