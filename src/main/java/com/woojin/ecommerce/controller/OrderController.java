package com.woojin.ecommerce.controller;

import com.woojin.ecommerce.common.response.BaseResponse;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.CreateOrderResponse;
import com.woojin.ecommerce.dto.OrderListResponse;
import com.woojin.ecommerce.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "주문", description = "주문 관련 API")
public class OrderController {
    private final OrderService orderService;

    @Operation(
            summary = "주문 생성",
            description = "유저가 상품을 주문합니다. (주문 수량 1개 이상)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "404", description = "상품 정보 찾을 수 없음")
    })
    @PostMapping("/orders")
    public BaseResponse<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return BaseResponse.ok(orderService.createOrder(request));
    }

    @Operation(
            summary = "주문 내역 조회",
            description = "유저별 주문 내역을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 내역 성공")
    })
    @GetMapping("/users/{userId}/orders")
    public BaseResponse<Page<OrderListResponse>> getOrderList(
            @PathVariable Long userId,
            Pageable pageable) {
        return BaseResponse.ok(orderService.getOrderList(userId, pageable));
    }

    @Operation(
            summary = "주문 취소",
            description = "유저가 한 주문을 취소합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "주문 취소 성공"),
            @ApiResponse(responseCode = "403", description = "주문 권한 없음"),
            @ApiResponse(responseCode = "404", description = "주문 정보 없음"),
            @ApiResponse(responseCode = "409", description = "이미 취소된 주문")
    })
    @PatchMapping("/orders/{orderId}/cancel")
    public BaseResponse<Void> cancelOrder(
            @PathVariable Long orderId,
            @RequestHeader("USER-ID") Long userId
    ) {
        orderService.cancelOrder(orderId, userId);
        return BaseResponse.ok(null);
    }
}
