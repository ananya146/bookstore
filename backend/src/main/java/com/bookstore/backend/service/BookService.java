package com.bookstore.backend.service;

import com.bookstore.backend.entity.Book;
import com.bookstore.backend.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // Get every book in the catalogue
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get details of one book
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    // Get books belonging to a selected category
    public List<Book> getBooksByCategory(Long categoryId) {
        return bookRepository.findByCategoryId(categoryId);
    }

    // Get books belonging to a selected brand
    public List<Book> getBooksByBrand(Long brandId) {
        return bookRepository.findByBrandId(brandId);
    }
}
