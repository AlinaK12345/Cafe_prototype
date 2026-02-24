package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByActiveTrue();

    @Query("SELECT p FROM Product p WHERE p.category.name = :categoryName AND p.active = true")
    List<Product> findByCategoryNameAndActiveTrue(@Param("categoryName") String categoryName);
}