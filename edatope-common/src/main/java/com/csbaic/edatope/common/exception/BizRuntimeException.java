package com.csbaic.edatope.common.exception;

import com.csbaic.edatope.common.result.ResultCode;

/**
 * 业务异常类
 */
public class BizRuntimeException extends RuntimeException  {

    private static final long serialVersionUID = -1827198664611457387L;

    /**
     * 异常码
     */
    private final String code ;

    /**
     * 响应数据
     */
    private final Object data;



    public BizRuntimeException(String message) {
        this(ResultCode.ERROR.getCode(), message);
    }

    public BizRuntimeException(String errorCode, String message) {
        this(errorCode, message, null);
    }


    public BizRuntimeException(String errorCode, String message, Object data){
        super(message);
        this.code = errorCode;
        this.data = data;
    }


    public BizRuntimeException(Throwable cause) {
        super(cause);
        code = ResultCode.ERROR.getCode();
        data = null;
    }


    public String getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public static BizRuntimeException from(ResultCode code){
        return from(code, code.getMessage(), null);
    }

    public static BizRuntimeException from(ResultCode code, Object data){
        return from(code, code.getMessage(), data);
    }


    public static BizRuntimeException from(ResultCode code, String message){
        return from(code, message, null);
    }


    public static BizRuntimeException from(ResultCode code, String message, Object data){
        return new BizRuntimeException(code.getCode(), message != null ? message : code.getMessage(), data);
    }
}
