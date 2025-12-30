package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class ProductNotFoundException extends ApiException {
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
}
