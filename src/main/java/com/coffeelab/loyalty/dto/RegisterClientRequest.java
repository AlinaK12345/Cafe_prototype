package com.coffeelab.loyalty.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterClientRequest {
    @NotBlank(message = "Phone is required")
    public String phone;

    public String fullName;

    // Getters and Setters
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
}