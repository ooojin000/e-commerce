package com.woojin.ecommerce.service;

import com.woojin.ecommerce.common.exception.OrderAccessDeniedException;
import com.woojin.ecommerce.common.exception.OrderAlreadyCanceledException;
import com.woojin.ecommerce.common.exception.OrderNotFoundException;
import com.woojin.ecommerce.common.exception.ProductNotFoundException;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.CreateOrderResponse;
import com.woojin.ecommerce.dto.OrderListResponse;
import com.woojin.ecommerce.entity.Order;
import com.woojin.ecommerce.entity.OrderStatus;
import com.woojin.ecommerce.entity.Product;
import com.woojin.ecommerce.repository.OrderRepository;
import com.woojin.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequest request) {
        Product product = getValidProduct(request.getProductId());
        product.decreaseStock(request.getQuantity());

        Order order = new Order(product, request.getUserId(), request.getQuantity());
        return CreateOrderResponse.from(orderRepository.save(order));
    }

    @Transactional(readOnly = true)
    public Page<OrderListResponse> getOrderList(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(OrderListResponse::from);
    }

    @Transactional
    public void cancelOrder(Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);

        if (!order.getUserId().equals(userId)) {
            throw new OrderAccessDeniedException();
        }

        if (order.getStatus() == OrderStatus.CANCELED) {
            throw new OrderAlreadyCanceledException();
        }

        Long productId = order.getProduct().getId();
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        product.increaseStock(order.getQuantity());
        order.cancel();
    }

    private Product getValidProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
