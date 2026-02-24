package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}