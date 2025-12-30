package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class OrderAccessDeniedException extends ApiException {
    public OrderAccessDeniedException() {
        super(ErrorCode.ORDER_ACCESS_DENIED);
    }
}
