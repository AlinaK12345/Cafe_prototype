package com.coffeelab.loyalty.repo;

import com.coffeelab.loyalty.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByPhone(String phone);
    boolean existsByPhone(String phone);
}