package com.coffeelab.loyalty.service;

import com.coffeelab.loyalty.model.Product;
import com.coffeelab.loyalty.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final ProductRepository productRepository;

    public MenuService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listActive(String category) {
        if (category == null || category.isBlank()) {
            return productRepository.findByActiveTrue();
        }
        return productRepository.findByCategoryNameAndActiveTrue(category);
    }
}