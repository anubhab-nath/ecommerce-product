package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.FakeStoreProductDto;
import dev.anubhav.product_catalog.dtos.ProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Primary
@Component("fakeStoreProductService")
public class FakeStoreProductService implements ProductService {

    private final RestTemplateBuilder restTemplateBuilder;
    private final String productRequestUrl = "https://fakestoreapi.com/products";

    @Autowired
    public FakeStoreProductService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public ProductDto getProductById(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String requestUrl = productRequestUrl + "/{id}";

        ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(requestUrl, FakeStoreProductDto.class, id);
        return convertToProductDto(response.getBody());
    }

    @Override
    public List<ProductDto> getAllProducts(Integer limit, String sort) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("sort", sort);

        String requestUrl = setQueryParam(productRequestUrl, params);
        ResponseEntity<FakeStoreProductDto[]> response = restTemplate.getForEntity(requestUrl, FakeStoreProductDto[].class);

        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(this::convertToProductDto)
                .toList();
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        ResponseEntity<FakeStoreProductDto> response = restTemplate.postForEntity(productRequestUrl, productDto, FakeStoreProductDto.class);
        return convertToProductDto(response.getBody());
    }

    @Override
    public List<String> getAllCategories() {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String requestUrl = productRequestUrl + "/categories";

        ResponseEntity<String[]> response = restTemplate.getForEntity(requestUrl, String[].class);
        return Arrays.stream(Objects.requireNonNull(response.getBody()))
                .toList();
    }

    @Override
    public ProductDto updateProduct() {
        return null;
    }

    private String setQueryParam(String requestUrl, Map<String, Object> params) {
        StringBuilder queryString = new StringBuilder();
        for(Map.Entry<String, Object> param: params.entrySet()) {
            String key = param.getKey();
            Object value = param.getValue();
            if(value != null) {
                // ?param=3&
                queryString.append(queryString.isEmpty() ? "?" : "&")
                        .append(key).append("=").append(value);
            }
        }

        return requestUrl + queryString;
    }

    private ProductDto convertToProductDto(FakeStoreProductDto fakeStoreProductDto) {
        return ProductDto.builder()
                .id(Objects.requireNonNull(fakeStoreProductDto).getId())
                .title(fakeStoreProductDto.getTitle())
                .price(fakeStoreProductDto.getPrice())
                .description(fakeStoreProductDto.getDescription())
                .category(fakeStoreProductDto.getCategory())
                .image(fakeStoreProductDto.getImage())
                .build();
    }
}
