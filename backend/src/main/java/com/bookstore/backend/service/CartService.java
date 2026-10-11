package com.bookstore.backend.service;

import com.bookstore.backend.entity.*;
import com.bookstore.backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public CartService(CartRepository cartRepository,
                       CartItemRepository cartItemRepository,
                       UserRepository userRepository,
                       BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    // Get all items currently in a user's cart
    public List<CartItem> getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        return cartItemRepository.findByCartId(cart.getId());
    }

    // Add a book to the user's cart
    public CartItem addToCart(Long userId, Long bookId, Integer quantity) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        if (book.getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        // Create a cart for the user if they don't have one yet
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> cartRepository.save(new Cart(user)));

        // Check if this book is already in the cart
        List<CartItem> existingItems =
                cartItemRepository.findByCartId(cart.getId());

        for (CartItem existingItem : existingItems) {

            if (existingItem.getBook().getId().equals(bookId)) {

                int newQuantity =
                        existingItem.getQuantity() + quantity;

                if (book.getStock() < newQuantity) {
                    throw new RuntimeException("Not enough stock");
                }

                existingItem.setQuantity(newQuantity);

                return cartItemRepository.save(existingItem);
            }
        }

        // Book is not already in the cart, so create a new cart item
        CartItem item = new CartItem(cart, book, quantity);

        return cartItemRepository.save(item);
    }

    // Change the quantity of an existing cart item
    public CartItem updateQuantity(Long itemId, Integer quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than zero");
        }

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (item.getBook().getStock() < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        item.setQuantity(quantity);

        return cartItemRepository.save(item);
    }

    // Remove a book from the cart
    public void removeFromCart(Long itemId) {
        if (!cartItemRepository.existsById(itemId)) {
            throw new RuntimeException("Cart item not found");
        }

        cartItemRepository.deleteById(itemId);
    }
}
