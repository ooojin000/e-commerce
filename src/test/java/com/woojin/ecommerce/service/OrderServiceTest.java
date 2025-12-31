package com.woojin.ecommerce.service;

import com.woojin.ecommerce.common.exception.OrderAccessDeniedException;
import com.woojin.ecommerce.common.exception.OrderAlreadyCanceledException;
import com.woojin.ecommerce.common.exception.OutOfStockException;
import com.woojin.ecommerce.common.exception.ProductNotFoundException;
import com.woojin.ecommerce.dto.CreateOrderRequest;
import com.woojin.ecommerce.dto.OrderListResponse;
import com.woojin.ecommerce.entity.Order;
import com.woojin.ecommerce.entity.OrderStatus;
import com.woojin.ecommerce.entity.Product;
import com.woojin.ecommerce.repository.OrderRepository;
import com.woojin.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrderService 단위 테스트")
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepository, productRepository);
    }

    // 주문 생성
    @Test
    @DisplayName("주문 생성 시 재고를 차감하고 주문을 저장한다")
    void createOrder_success() {
        // given
        Product product = productWith(1L, "장난감", 1000, 10);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class)))
                .thenReturn(new Order(product, 2L, 3));

        // when
        orderService.createOrder(req(2L, 1L, 3));

        // then
        assertThat(product.getStock()).isEqualTo(7);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("상품 ID가 없으면 예외가 발생한다")
    void createOrder_productNotFound() {
        // given
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> orderService.createOrder(req(2L, 999L, 1)))
                .isInstanceOf(ProductNotFoundException.class);

        // then
        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("재고 부족이면 예외가 발생한다")
    void createOrder_outOfStock() {
        // given
        Product product = productWith(1L, "장난감", 1000, 1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        assertThatThrownBy(() -> orderService.createOrder(req(2L, 1L, 2)))
                .isInstanceOf(OutOfStockException.class);

        // then
        verify(orderRepository, never()).save(any());
        assertThat(product.getStock()).isEqualTo(1);
    }

    // 주문 내역 조회
    @Test
    @DisplayName("유저가 주문 내역을 조회한다")
    void getOrderList_returnsPage() {
        // given
        Product product = productWith(1L, "장난감", 5000, 10);
        Order order = new Order(product, 2L, 2);

        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findByUserId(2L, pageable))
                .thenReturn(new PageImpl<>(List.of(order), pageable, 1));

        // when
        Page<OrderListResponse> res = orderService.getOrderList(2L, pageable);

        // then
        assertThat(res.getContent()).hasSize(1);
        assertThat(res.getContent().get(0).getProductId()).isEqualTo(1L);
        assertThat(res.getContent().get(0).getQuantity()).isEqualTo(2);
    }

    // 주문 취소
    @Test
    @DisplayName("주문을 취소하면 재고가 복구되고 주문 상태가 CANCELED로 변경된다")
    void cancelOrder_success_restoresStockAndCancels() {
        // given
        Product product = productWith(1L, "장난감", 1000, 10);
        Order order = new Order(product, 2L, 3);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        // when
        orderService.cancelOrder(5L, 2L);

        // then
        assertThat(product.getStock()).isEqualTo(13);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThat(order.getCanceledAt()).isNotNull();
    }

    @Test
    @DisplayName("주문 유저와 주문 취소 요청 유저가 다르면 예외가 발생한다")
    void cancelOrder_accessDenied() {
        // given
        Product product = productWith(1L, "장난감", 1000, 10);
        Order order = new Order(product, 2L, 3);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.cancelOrder(5L, 999L))
                .isInstanceOf(OrderAccessDeniedException.class);

        verify(productRepository, never()).findById(any());
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
    }

    @Test
    @DisplayName("이미 취소된 주문을 또 취소하려고 하면 예외가 발생한다")
    void cancelOrder_alreadyCanceled() {
        // given
        Product product = productWith(1L, "장난감", 1000, 10);
        Order order = new Order(product, 2L, 3);
        setField(order, "status", OrderStatus.CANCELED);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        // when & then
        assertThatThrownBy(() -> orderService.cancelOrder(5L, 2L))
                .isInstanceOf(OrderAlreadyCanceledException.class);
    }

    private Product productWith(Long id, String name, int price, int stock) {
        Product p = new Product();
        setField(p, "id", id);
        setField(p, "name", name);
        setField(p, "price", price);
        setField(p, "stock", stock);
        return p;
    }

    private CreateOrderRequest req(Long userId, Long productId, int quantity) {
        CreateOrderRequest req = new CreateOrderRequest();
        req.setUserId(userId);
        req.setProductId(productId);
        req.setQuantity(quantity);
        return req;
    }
}