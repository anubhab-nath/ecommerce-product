package dev.anubhav.product_catalog.dtos;

import dev.anubhav.product_catalog.models.Price;
import dev.anubhav.product_catalog.models.Product;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class ProductDto {
    private String id;
    private String title;
    private PriceDto price;
    private String category;
    private String description;
    private String image;

    public static ProductDto from(Product product) {
        Price price = product.getPrice();
        return ProductDto.builder()
                .id(product.getId().toString())
                .title(product.getTitle())
                .category(product.getCategory().getName())
                .image(product.getImage())
                .price(PriceDto.builder()
                        .currency(price.getCurrency())
                        .amount(price.getAmount()).build()
                )
                .description(product.getDescription())
                .build();
    }

    public static ProductDto from(FakeStoreProductDto fakeStoreProductDto) {
        return ProductDto.builder()
                .id(Objects.requireNonNull(fakeStoreProductDto).getId().toString())
                .title(fakeStoreProductDto.getTitle())
                .price(PriceDto.builder()
                        .currency("INR")
                        .amount(fakeStoreProductDto.getPrice())
                        .build()
                )
                .description(fakeStoreProductDto.getDescription())
                .category(fakeStoreProductDto.getCategory())
                .image(fakeStoreProductDto.getImage())
                .build();
    }
}
