package com.woojin.ecommerce.controller;

import com.woojin.ecommerce.common.response.BaseResponse;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.CreateOrderResponse;
import com.woojin.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
