package com.woojin.ecommerce.dto;

import com.woojin.ecommerce.entity.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "주문 생성 응답 DTO")
public class CreateOrderResponse {
    @Schema(description = "주문 ID", example = "123")
    private Long orderId;

    @Schema(description = "유저 ID", example = "1")
    private Long userId;

    @Schema(description = "상품 ID", example = "45")
    private Long productId;

    @Schema(description = "주문 수량", example = "2")
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
