package com.woojin.ecommerce.dto;

import com.woojin.ecommerce.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "주문 내역 응답 DTO")
public class OrderListResponse {
    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer totalPrice;
    private Integer quantity;

    public static OrderListResponse from(Order o) {
        Integer price = o.getProduct().getPrice();
        Integer quantity = o.getQuantity();

        Integer totalPrice = price * quantity;

        return new OrderListResponse(
                o.getId(),
                o.getUserId(),
                o.getProduct().getId(),
                o.getProduct().getName(),
                totalPrice,
                o.getQuantity()
        );
    }
}
