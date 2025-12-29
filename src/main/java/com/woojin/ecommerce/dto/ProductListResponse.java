package com.woojin.ecommerce.dto;

import com.woojin.ecommerce.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListResponse {
    private Long id;
    private String name;
    private int price;
    private int stock;

    public static ProductListResponse from(Product p) {
        return new ProductListResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock());
    }
}
