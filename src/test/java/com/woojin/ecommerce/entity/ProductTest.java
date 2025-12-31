package com.woojin.ecommerce.entity;

import com.woojin.ecommerce.common.exception.InvalidQuantityException;
import com.woojin.ecommerce.common.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DisplayName("Product 도메인 단위 테스트")
class ProductTest {
    private Product product;

    @BeforeEach
    void setUp() {
        // given
        product = productWithStock(10);
    }

    @Test
    @DisplayName("정상 주문이면 재고가 감소한다")
    void decreaseStock_success_decreasesExactly() {
        // when
        product.decreaseStock(4);

        // then
        assertThat(product.getStock()).isEqualTo(6);
    }

    @Test
    @DisplayName("주문 수량이 0 이하이면 예외가 발생한다")
    void decreaseStock_invalidQuantity_throwsException() {
        // when
        assertThatThrownBy(() -> product.decreaseStock(0))
                .isInstanceOf(InvalidQuantityException.class);

        assertThatThrownBy(() -> product.decreaseStock(-1))
                .isInstanceOf(InvalidQuantityException.class);

        // then
        assertThat(product.getStock()).isEqualTo(10);
    }

    @Test
    @DisplayName("상품 재고가 부족하면 예외가 발생한다")
    void decreaseStock_outOfStock_throwsException() {
        // given
        Product lowStockProduct = productWithStock(2);

        // when
        assertThatThrownBy(() -> lowStockProduct.decreaseStock(3))
                .isInstanceOf(OutOfStockException.class);

        // then
        assertThat(lowStockProduct.getStock()).isEqualTo(2);
    }

    @Test
    @DisplayName("재고 복구 시 재고가 증가한다")
    void increaseStock_success_increasesExactly() {
        product.increaseStock(3);
        assertThat(product.getStock()).isEqualTo(13);
    }

    private Product productWithStock(int stock) {
        Product p = new Product();
        setField(p, "stock", stock);
        return p;
    }
}