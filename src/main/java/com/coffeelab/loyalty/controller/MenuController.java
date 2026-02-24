package com.coffeelab.loyalty.controller;

import com.coffeelab.loyalty.model.Product;
import com.coffeelab.loyalty.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/products")
    public List<Product> getProducts(@RequestParam(required = false) String category) {
        return menuService.listActive(category);
    }
}