package com.coffeelab.loyalty.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class AddItemRequest {
    @NotNull(message = "Product ID is required")
    public Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    public Integer qty;

    // Getters and Setters
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Integer getQty() { return qty; }
    public void setQty(Integer qty) { this.qty = qty; }
}