
package com.bookstore.backend.repository;

import com.bookstore.backend.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserId(Long userId);

    // Check whether an existing order uses this address.
    boolean existsByAddressId(Long addressId);
}
