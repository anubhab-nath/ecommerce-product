package dev.anubhav.product_catalog.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {
    private String id;
    private String title;
    private String currency;
    private double price;
    private String category;
    private String description;
    private String image;
}
