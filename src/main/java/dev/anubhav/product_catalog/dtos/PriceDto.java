package dev.anubhav.product_catalog.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PriceDto {
    private String currency;
    private double amount;
}
