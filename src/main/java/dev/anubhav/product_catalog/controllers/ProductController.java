package dev.anubhav.product_catalog.controllers;

import dev.anubhav.product_catalog.dtos.ExceptionDto;
import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;
import dev.anubhav.product_catalog.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts(
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "sort", required = false) String sort
    ) {
        return productService.getAllProducts(category, limit, sort);
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable("id") Long id) throws NotFoundException {
        return productService.getProductById(id);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto requestDto) {
        return productService.createProduct(requestDto);
    }

    @GetMapping("/categories")
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }

    @PutMapping("/{id}")
    public ProductDto updateProduct(
            @PathVariable("id") Long id,
            @RequestBody ProductDto requestDto
    ) {
        return productService.updateProduct(requestDto, id);
    }

    @DeleteMapping("/{id}")
    public ProductDto deleteProduct(@PathVariable("id") Long id) {
        return productService.deleteProduct(id);
    }

}
