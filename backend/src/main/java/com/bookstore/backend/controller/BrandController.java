package com.bookstore.backend.controller;

import com.bookstore.backend.entity.Brand;
import com.bookstore.backend.repository.BrandRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/brands")
@CrossOrigin(origins = "http://localhost:4200")
public class BrandController {

    private final BrandRepository brandRepository;

    public BrandController(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public List<Brand> getBrands() {
        return brandRepository.findAll();
    }
}
