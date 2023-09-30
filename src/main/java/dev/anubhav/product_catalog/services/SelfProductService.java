package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("ProductService")
public class SelfProductService implements ProductService {

    @Override
    public ProductDto getProductById(String id) throws NotFoundException {
        return null;
    }

    @Override
    public List<ProductDto> getAllProducts(String category, Integer limit, String sort) {
        return null;
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        return null;
    }

    @Override
    public List<String> getAllCategories() {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String id) {
        return null;
    }

    @Override
    public ProductDto deleteProduct(String id) {
        return null;
    }
}
