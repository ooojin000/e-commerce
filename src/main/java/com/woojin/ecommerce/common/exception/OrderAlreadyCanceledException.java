package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class OrderAlreadyCanceledException extends ApiException {
    public OrderAlreadyCanceledException() {
        super(ErrorCode.ORDER_ALREADY_CANCELED);
    }
}
