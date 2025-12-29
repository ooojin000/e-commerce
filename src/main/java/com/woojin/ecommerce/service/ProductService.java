package com.woojin.ecommerce.service;

import com.woojin.ecommerce.dto.ProductListResponse;
import com.woojin.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductListResponse> getProductList(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductListResponse::from);
    }
}
