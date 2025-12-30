package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class InvalidQuantityException extends ApiException {
    public InvalidQuantityException() {
        super(ErrorCode.INVALID_QUANTITY);
    }
}
