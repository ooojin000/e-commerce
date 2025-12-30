package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class OutOfStockException extends ApiException {
    public OutOfStockException() {
        super(ErrorCode.OUT_OF_STOCK);
    }
}
