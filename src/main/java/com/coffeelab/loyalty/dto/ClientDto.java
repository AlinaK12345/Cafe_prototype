package com.coffeelab.loyalty.dto;

import java.math.BigDecimal;

public class ClientDto {
    public Long id;
    public String phone;
    public String fullName;
    public BigDecimal bonusBalance;

    // Constructors
    public ClientDto() {}

    public ClientDto(Long id, String phone, String fullName, BigDecimal bonusBalance) {
        this.id = id;
        this.phone = phone;
        this.fullName = fullName;
        this.bonusBalance = bonusBalance;
    }
}