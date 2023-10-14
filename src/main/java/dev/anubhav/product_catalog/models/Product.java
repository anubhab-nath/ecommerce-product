package dev.anubhav.product_catalog.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity(name = "products")
public class Product extends BaseModel {
    private String title;
    private String description;
    private String image;

    @OneToOne
    private Price price;

    @ManyToOne
    private Category category;
}
