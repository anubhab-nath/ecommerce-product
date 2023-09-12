package dev.anubhav.product_catalog.entities;

import jakarta.persistence.Entity;
import lombok.Builder;

@Builder
public class Product extends BaseModel {
    private String title;
    private String description;
    private double price;
    private Category category;
}
