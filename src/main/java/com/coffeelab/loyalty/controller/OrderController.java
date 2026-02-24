package com.coffeelab.loyalty.controller;

import com.coffeelab.loyalty.dto.*;
import com.coffeelab.loyalty.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderDto createOrder(@RequestBody @Valid CreateOrderRequest request) {
        return orderService.createOrder(request.getCreatedByUserId(), request.getPhone());
    }

    @PostMapping("/{orderId}/items")
    public OrderDto addItem(@PathVariable Long orderId, @RequestBody @Valid AddItemRequest request) {
        return orderService.addItem(orderId, request.getProductId(), request.getQty());
    }

    @PostMapping("/{orderId}/spend-bonus")
    public OrderDto spendBonus(@PathVariable Long orderId, @RequestBody @Valid SpendBonusRequest request) {
        return orderService.spendBonus(orderId, request.getAmount());
    }

    @PostMapping("/{orderId}/complete")
    public OrderDto completeOrder(@PathVariable Long orderId) {
        return orderService.completeOrder(orderId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, Object> handleIllegalState(IllegalStateException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Conflict");
        response.put("message", e.getMessage());
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleGenericError(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", e.getClass().getSimpleName());
        response.put("message", e.getMessage());
        return response;
    }
}