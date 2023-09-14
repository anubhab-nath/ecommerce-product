package dev.anubhav.product_catalog.entities;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Product extends BaseModel {
    private String title;
    private String description;
    private double price;
    private Category category;
}
