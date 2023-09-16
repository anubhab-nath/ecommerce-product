package dev.anubhav.product_catalog.services;

import dev.anubhav.product_catalog.dtos.ExceptionDto;
import dev.anubhav.product_catalog.dtos.FakeStoreProductDto;
import dev.anubhav.product_catalog.dtos.ProductDto;
import dev.anubhav.product_catalog.exceptions.NotFoundException;
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
    public ProductDto getProductById(Long id) throws NotFoundException {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String requestUrl = productRequestUrl + "/{id}";

        ResponseEntity<FakeStoreProductDto> response = restTemplate.getForEntity(requestUrl, FakeStoreProductDto.class, id);

        FakeStoreProductDto fakeStoreProductDto = response.getBody();
        if(fakeStoreProductDto == null)
            throw new NotFoundException("Product with id: " + id + "not found");
        return convertToProductDto(fakeStoreProductDto);
    }

    @Override
    public List<ProductDto> getAllProducts(String category, Integer limit, String sort) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        // non-modular
        String categoryUrl = category == null ? productRequestUrl : productRequestUrl + "/category/" + category;

        Map<String, Object> params = new HashMap<>();
        params.put("limit", limit);
        params.put("sort", sort);

        String requestUrl = setQueryParam(categoryUrl, params);
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
    public ProductDto updateProduct(ProductDto productDto, Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String requestUrl = productRequestUrl + "/{id}";

        HttpEntity<ProductDto> httpEntity = new HttpEntity<>(productDto);
        ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(requestUrl, HttpMethod.PUT, httpEntity, FakeStoreProductDto.class, id);
        return convertToProductDto(response.getBody());
    }

    @Override
    public ProductDto deleteProduct(Long id) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        String requestUrl = productRequestUrl + "/{id}";

        ResponseEntity<FakeStoreProductDto> response = restTemplate.exchange(requestUrl, HttpMethod.DELETE, HttpEntity.EMPTY, FakeStoreProductDto.class, id);
        return convertToProductDto(response.getBody());
    }

    private String setQueryParam(String requestUrl, Map<String, Object> params) {
        StringBuilder queryString = new StringBuilder();
        for(Map.Entry<String, Object> param: params.entrySet()) {
            String key = param.getKey();
            Object value = param.getValue();
            if(value != null) {
                // ?param=1&param=2
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
