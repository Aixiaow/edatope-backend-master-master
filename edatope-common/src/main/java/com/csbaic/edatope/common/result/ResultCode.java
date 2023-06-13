package com.csbaic.edatope.common.result;

public interface ResultCode {

    /**
     * 操作成功
     */
    ResultCode SUCCESS =  new DefaultResultCode("SUCCESS", "操作成功", true);

    /**
     * 操作成功
     */
    ResultCode ERROR =  new DefaultResultCode("ERROR", "操作失败", false);


    /**
     * 请求错误
     */
    ResultCode BAD_REQUEST = new DefaultResultCode("BAD_REQUEST", "请求错误", false);


    /**
     * 资源未找到
     */
    ResultCode NOT_FOUND = new DefaultResultCode("NOT_FOUND", "请求数据未找到", false);



    /**
     * 响应结果 Code
     * @return
     */
    String getCode();

    /**
     * 响应消息
     * @return
     */
    String getMessage();

    /**
     * 是否成功
     * @return
     */
    Boolean isSuccess();


    /**
     * 生成一个 {@link ResultCode} 实例
     * @param code
     * @param message
     * @return
     */
    static DefaultResultCode error(String code, String message){
        return new DefaultResultCode(code, message, false);
    }


    /**
     * 生成一个 {@link ResultCode} 实例
     * @param code
     * @param message
     * @return
     */
    static ResultCode ok(String code, String message){
        return new DefaultResultCode(code, message, true);
    }


}
