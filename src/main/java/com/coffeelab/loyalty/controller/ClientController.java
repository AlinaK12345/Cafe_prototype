package com.coffeelab.loyalty.controller;

import com.coffeelab.loyalty.dto.ClientDto;
import com.coffeelab.loyalty.dto.RegisterClientRequest;
import com.coffeelab.loyalty.repo.BonusTxnRepository;
import com.coffeelab.loyalty.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    private final ClientService clientService;
    private final BonusTxnRepository bonusTxnRepository;

    public ClientController(ClientService clientService, BonusTxnRepository bonusTxnRepository) {
        this.clientService = clientService;
        this.bonusTxnRepository = bonusTxnRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDto register(@RequestBody @Valid RegisterClientRequest request) {
        return clientService.register(request.getPhone(), request.getFullName());
    }

    @GetMapping("/by-phone/{phone}")
    public ClientDto findByPhone(@PathVariable String phone) {
        return clientService.findByPhone(phone);
    }

    @GetMapping("/{clientId}/bonus-history")
    public Object getBonusHistory(@PathVariable Long clientId) {
        return bonusTxnRepository.findByClient_IdOrderByCreatedAtDesc(clientId);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleIllegalArgument(IllegalArgumentException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", "Bad Request");
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