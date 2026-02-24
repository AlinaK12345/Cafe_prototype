package com.coffeelab.loyalty.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "client", uniqueConstraints = @UniqueConstraint(columnNames = "phone"))
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String phone;

    private String fullName;

    @Column(nullable = false)
    private BigDecimal bonusBalance = BigDecimal.ZERO;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // Constructors
    public Client() {}

    public Client(String phone, String fullName) {
        this.phone = phone;
        this.fullName = fullName;
        this.bonusBalance = BigDecimal.ZERO;
        this.createdAt = Instant.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public BigDecimal getBonusBalance() { return bonusBalance; }
    public void setBonusBalance(BigDecimal bonusBalance) { this.bonusBalance = bonusBalance; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}