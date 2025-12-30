package com.woojin.ecommerce.entity;

import com.woojin.ecommerce.common.exception.InvalidQuantityException;
import com.woojin.ecommerce.common.exception.OutOfStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String name;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer price;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer stock;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void decreaseStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidQuantityException();
        }
        if (this.stock < quantity) {
            throw new OutOfStockException();
        }
        this.stock -= quantity;
    }
}
