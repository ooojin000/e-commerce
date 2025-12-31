package com.woojin.ecommerce.common.response;

import com.woojin.ecommerce.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "API 기본 응답 형식")
public class BaseResponse<T> {
    @Schema(description = "응답 성공 여부")
    private boolean success;

    @Schema(description = "응답 코드")
    private String code;

    @Schema(description = "응답 메시지")
    private String message;

    @Schema(description = "실제 데이터 응답")
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
