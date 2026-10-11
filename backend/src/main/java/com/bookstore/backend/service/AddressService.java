
package com.bookstore.backend.service;

import com.bookstore.backend.entity.Address;
import com.bookstore.backend.entity.User;
import com.bookstore.backend.repository.AddressRepository;
import com.bookstore.backend.repository.OrderRepository;
import com.bookstore.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public AddressService(
            AddressRepository addressRepository,
            UserRepository userRepository,
            OrderRepository orderRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    // Get all saved addresses for a user.
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    // Save a new delivery address.
    public Address addAddress(Long userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        address.setUser(user);
        return addressRepository.save(address);
    }

    // Delete an address only if it belongs to the user and no order uses it.
    public void deleteAddress(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Address not found"));

        if (address.getUser() == null
                || !address.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot delete another user's address");
        }

        if (orderRepository.existsByAddressId(addressId)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This address is linked to an existing order and cannot be deleted");
        }

        addressRepository.delete(address);
    }
}
