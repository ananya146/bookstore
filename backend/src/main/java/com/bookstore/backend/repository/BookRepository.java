package com.bookstore.backend.repository;

import com.bookstore.backend.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    // Find books in a selected category
    List<Book> findByCategoryId(Long categoryId);

    // Find books from a selected brand
    List<Book> findByBrandId(Long brandId);
}
