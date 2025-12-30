package com.woojin.ecommerce.dto;

import com.woojin.ecommerce.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreateOrderResponse {
    private Long orderId;
    private Long userId;
    private Long productId;
    private Integer quantity;

    public static CreateOrderResponse from(Order order) {
        return new CreateOrderResponse(
                order.getId(),
                order.getUserId(),
                order.getProduct().getId(),
                order.getQuantity()
        );
    }
}
