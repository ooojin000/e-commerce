package com.woojin.ecommerce.service;

import com.woojin.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService 단위 테스트")
class ProductServiceTest {
    @Mock ProductRepository productRepository;

    ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService(productRepository);
    }

    @Test
    void getProductList_callsRepository() {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable))
                .thenReturn(Page.empty(pageable));

        // when
        productService.getProductList(pageable);

        // then
        verify(productRepository).findAll(pageable);
    }
}