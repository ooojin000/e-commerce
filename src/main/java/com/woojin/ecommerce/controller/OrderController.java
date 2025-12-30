package com.woojin.ecommerce.controller;

import com.woojin.ecommerce.common.response.BaseResponse;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.CreateOrderResponse;
import com.woojin.ecommerce.dto.OrderListResponse;
import com.woojin.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/orders")
    public BaseResponse<CreateOrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request) {
        return BaseResponse.ok(orderService.createOrder(request));
    }

    @GetMapping("/users/{userId}/orders")
    public BaseResponse<Page<OrderListResponse>> getOrderList(
            @PathVariable Long userId,
            Pageable pageable) {
        return BaseResponse.ok(orderService.getOrderList(userId, pageable));
    }
}
