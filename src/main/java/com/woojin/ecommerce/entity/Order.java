package com.woojin.ecommerce.entity;

import com.woojin.ecommerce.common.exception.OrderAlreadyCanceledException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Table(name = "orders")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @NotNull
    @Column(nullable = false)
    private Long userId;

    @NotNull
    @Min(1)
    @Column(nullable = false)
    private Integer quantity;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime canceledAt;

    public void cancel() {
        if (status == OrderStatus.CANCELED) {
            throw new OrderAlreadyCanceledException();
        };
        this.status = OrderStatus.CANCELED;
        this.canceledAt = LocalDateTime.now();
    }

    public Order(Product product, Long userId, Integer quantity) {
        this.product = product;
        this.userId = userId;
        this.quantity = quantity;
        this.status = OrderStatus.ORDERED;
        this.createdAt = LocalDateTime.now();
    }
}
