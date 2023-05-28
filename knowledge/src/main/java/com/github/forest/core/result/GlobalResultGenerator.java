package com.github.forest.core.result;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author sunzy
 * @Date 2023/5/28 17:29
 */

@Slf4j
public class GlobalResultGenerator {

    public static <T> GlobalResult<T> genResult(boolean success, T data, String message) {
        GlobalResult<T> result = GlobalResult.newInstance();new GlobalResult<>();
        result.setSuccess(success);
        result.setData(data);
        result.setMessage(message);
        return result;
    }


    /**
     * success
     * @param data
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genSuccessResult(T data){
        return genResult(true, data, null);
    }


    /**
     * error message
     * @param message error message
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genErrorResult(String message){
        return genResult(true, null, message);
    }

    /**
     * success
     * @param errorCode
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genErrorResult(ErrorCode errorCode){
        return genErrorResult(errorCode.getMessage());
    }

    /**
     * success no message
     *
     * @return
     */
    public static GlobalResult genSuccessResult() {
        return genSuccessResult(null);
    }


    /**
     * success
     *
     * @param <T>
     * @return
     */
    public static <T> GlobalResult<T> genSuccessResult(String message) {

        return genResult(true, null, message);
    }


}
