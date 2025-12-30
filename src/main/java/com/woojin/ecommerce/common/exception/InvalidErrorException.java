package com.woojin.ecommerce.common.exception;

import com.woojin.ecommerce.global.exception.ApiException;
import com.woojin.ecommerce.global.exception.ErrorCode;

public class InvalidErrorException extends ApiException {
    public InvalidErrorException() {
        super(ErrorCode.INTERNAL_ERROR);
    }
}
