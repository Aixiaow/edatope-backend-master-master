package com.csbaic.edatope.common.result;

class DefaultResultCode  implements ResultCode {


    private final String code;
    private final String message;
    private final Boolean isSuccess;


    DefaultResultCode(String code, String message, Boolean isSuccess) {
        this.code = code;
        this.message = message;
        this.isSuccess = isSuccess;
    }


    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Boolean isSuccess() {
        return isSuccess;
    }



}
