package com.woojin.ecommerce.common.response;

import com.woojin.ecommerce.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BaseResponse<T> {
    private boolean success;
    private String code;
    private String message;
    private T data;

    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(true, "success", "성공", data);
    }

    public static <T> BaseResponse<T> fail (String code, String message) {
        return new BaseResponse<>(false, code, message, null);
    }

    public static <T> BaseResponse<T> fail(ErrorCode errorCode) {
        return new BaseResponse<>(false, errorCode.getCode(), errorCode.getMessage(), null);
    }
}
