package com.woojin.ecommerce.entity;

import com.woojin.ecommerce.common.exception.OrderAlreadyCanceledException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DisplayName("Order 도메인 단위 테스트")
class OrderTest {
    private Product product;
    private Order order;

    @BeforeEach
    void setUp() {
        // given
        product = productWithIdAndStock(1L, 10);
        order = new Order(product, 1L, 3);
    }

    @AfterEach
    void tearDown() {
        order = null;
        product = null;
    }

    @Test
    @DisplayName("주문 생성 시 상태는 ORDERED이고 canceledAt은 null이다")
    void createOrder_defaultStatus_isOrdered() {
        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
        assertThat(order.getCanceledAt()).isNull();

        assertThat(order.getUserId()).isEqualTo(1L);
        assertThat(order.getQuantity()).isEqualTo(3);
        assertThat(order.getProduct()).isSameAs(product);
    }

    @Test
    @DisplayName("주문 취소 시 상태는 CANCELED로 변경되고 canceledAt이 설정된다")
    void cancel_success_setsCanceledStatusAndCanceledAt() {
        // when
        order.cancel();

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThat(order.getCanceledAt()).isNotNull();
    }

    @Test
    @DisplayName("이미 취소된 주문을 다시 취소하면 예외가 발생한다")
    void cancel_twice_throwsOrderAlreadyCanceled() {
        // given
        order.cancel();

        // when
        assertThatThrownBy(() -> order.cancel())
                .isInstanceOf(OrderAlreadyCanceledException.class);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELED);
        assertThat(order.getCanceledAt()).isNotNull();
    }

    private Product productWithIdAndStock(Long id, int stock) {
        Product p = new Product();
        setField(p, "id", id);
        setField(p, "stock", stock);
        return p;
    }
}