package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {}