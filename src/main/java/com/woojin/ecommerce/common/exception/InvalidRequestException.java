package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class InvalidRequestException extends ApiException {
    public InvalidRequestException() {
        super(ErrorCode.INVALID_REQUEST);
    }
}
