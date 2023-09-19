package dev.anubhav.product_catalog.services.adapters;

import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;

import java.util.List;

public interface ThirdPartyClient {
    ProductDto getProductById(Long id) throws NotFoundException;

    List<ProductDto> getAllProducts(String category, Integer limit, String sort);

    ProductDto createProduct(ProductDto productDto);

    List<String> getAllCategories();

    ProductDto updateProduct(ProductDto productDto, Long id);

    ProductDto deleteProduct(Long id);
}
