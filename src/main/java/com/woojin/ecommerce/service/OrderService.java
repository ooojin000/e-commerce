package com.woojin.ecommerce.service;

import com.woojin.ecommerce.common.exception.ProductNotFoundException;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.CreateOrderResponse;
import com.woojin.ecommerce.entity.Order;
import com.woojin.ecommerce.entity.Product;
import com.woojin.ecommerce.repository.OrderRepository;
import com.woojin.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
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

    private Product getValidProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }
}
