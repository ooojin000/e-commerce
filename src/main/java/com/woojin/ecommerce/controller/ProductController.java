package com.woojin.ecommerce.controller;

import com.woojin.ecommerce.dto.ProductListResponse;
import com.woojin.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/api/products")
    public Page<ProductListResponse> getProductList(Pageable pageable) {
        return productService.getProductList(pageable);
    }
}
