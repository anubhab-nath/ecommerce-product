package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto getProductById(String id) throws NotFoundException;
    List<ProductDto> getAllProducts(String category, Integer limit, String sort) throws NotFoundException;
    ProductDto createProduct(ProductDto productDto);
    List<String> getAllCategories();
    ProductDto updateProduct(ProductDto productDto, String id);
    ProductDto deleteProduct(String id) throws NotFoundException;
}
