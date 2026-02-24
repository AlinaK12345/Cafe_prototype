package com.coffeelab.loyalty.dto;

import com.coffeelab.loyalty.model.OrderStatus;
import java.math.BigDecimal;

public class OrderDto {
    public Long id;
    public Long clientId;
    public OrderStatus status;
    public BigDecimal subtotal;
    public BigDecimal bonusSpent;
    public BigDecimal total;

    // Constructors
    public OrderDto() {}

    public OrderDto(Long id, Long clientId, OrderStatus status,
                    BigDecimal subtotal, BigDecimal bonusSpent, BigDecimal total) {
        this.id = id;
        this.clientId = clientId;
        this.status = status;
        this.subtotal = subtotal;
        this.bonusSpent = bonusSpent;
        this.total = total;
    }
}