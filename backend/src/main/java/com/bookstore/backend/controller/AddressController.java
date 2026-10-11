
package com.bookstore.backend.controller;

import com.bookstore.backend.entity.Address;
import com.bookstore.backend.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@CrossOrigin(origins = "http://localhost:4200")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/user/{userId}")
    public List<Address> getAddresses(@PathVariable Long userId) {
        return addressService.getUserAddresses(userId);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Address> addAddress(
            @PathVariable Long userId,
            @RequestBody Address address) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(addressService.addAddress(userId, address));
    }

    @DeleteMapping("/user/{userId}/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @PathVariable Long userId,
            @PathVariable Long addressId) {

        addressService.deleteAddress(userId, addressId);
        return ResponseEntity.noContent().build();
    }
}
