package com.github.forest.core.result;

import lombok.Data;

/**
 * @Author sunzy
 * @Date 2023/5/28 17:21
 */
@Data
public class GlobalResult<T> {
    private boolean success = false;

    private T data;

    private int code;
    private String message;

    public GlobalResult() {

    }

    public GlobalResult(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMessage();
    }

    public static <T> GlobalResult<T> newInstance() {
        return new GlobalResult<>();
    }
}
