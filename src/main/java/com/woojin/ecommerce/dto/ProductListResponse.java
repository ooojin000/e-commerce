package com.woojin.ecommerce.dto;

import com.woojin.ecommerce.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "상품 목록 응답 DTO")
public class ProductListResponse {
    private Long id;
    private String name;
    private Integer price;
    private Integer stock;

    public static ProductListResponse from(Product p) {
        return new ProductListResponse(
                p.getId(),
                p.getName(),
                p.getPrice(),
                p.getStock());
    }
}
