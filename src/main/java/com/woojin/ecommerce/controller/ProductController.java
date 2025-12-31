package com.woojin.ecommerce.controller;

import com.woojin.ecommerce.common.response.BaseResponse;
import com.woojin.ecommerce.dto.ProductListResponse;
import com.woojin.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품", description = "상품 관련 API")
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "상품 목록 조회",
            description = "유저가 상품 목록을 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공")
    })
    @GetMapping("/api/products")
    public BaseResponse<Page<ProductListResponse>> getProductList(Pageable pageable) {
        return BaseResponse.ok(productService.getProductList(pageable));
    }
}
