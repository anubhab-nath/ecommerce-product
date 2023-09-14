package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    ProductDto getProductById(Long id);
    List<ProductDto> getAllProducts(Integer limit, String sort);
    ProductDto createProduct(ProductDto productDto);
    List<String> getAllCategories();

    ProductDto updateProduct();
}
