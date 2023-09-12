package dev.anubhav.product_catalog.controllers;

import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping
    public void getAllProducts() {
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable("id") Long id) {
        return "product id: " + id;
    }

    @PostMapping
    public String createProduct() {
        return UUID.randomUUID().toString();
    }

}
