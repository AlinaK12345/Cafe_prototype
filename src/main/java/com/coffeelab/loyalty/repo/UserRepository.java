package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}