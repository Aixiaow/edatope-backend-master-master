package com.csbaic.edatope.common.result;


import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Objects;


/**
 * 标准的 Restful 请求响应实体。
 */
public class Result<T> implements Serializable {

    /**
     * 响应 Code
     */
    private String code;
    /**
     * 是否成功
     */
    private Boolean success;

    /**
     * 消息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;


    protected Result(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.success = Objects.equals(ResultCode.SUCCESS.getCode(), code);
    }

    protected Result(ResultCode resultCode, T data){
        this(resultCode.getCode(), resultCode.getMessage(), data);
    }

    public String getCode() {
        return code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public static <T> Result<T> ok(T data) {
        return new Result<T>(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> ok() {
        return new Result<T>(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> error() {
        return new Result<T>(ResultCode.ERROR.getCode(), "操作失败", null);
    }

    public static <T> Result<T> error(T data) {
        return new Result<T>(ResultCode.ERROR, data);
    }

    public static <T> Result<T> error(String message) {
        return new Result<T>(ResultCode.ERROR.getCode(), message, null);
    }

    public static <T> Result<T> error(String code, String message, T data) {
        return new Result<T>(code, message, data);
    }


    public static <T> Result<T> error(String code, String message){
        return new Result<T>(code, message, null);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
