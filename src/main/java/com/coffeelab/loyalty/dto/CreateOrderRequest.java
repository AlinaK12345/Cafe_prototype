package com.coffeelab.loyalty.dto;

import jakarta.validation.constraints.NotNull;

public class CreateOrderRequest {
    @NotNull(message = "User ID is required")
    public Long createdByUserId;

    public String phone;

    // Getters and Setters
    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}