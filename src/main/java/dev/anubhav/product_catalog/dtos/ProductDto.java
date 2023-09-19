package dev.anubhav.product_catalog.dtos;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProductDto {
    private Long id;
    private String title;
    private double price;
    private String category;
    private String description;
    private String image;
}
