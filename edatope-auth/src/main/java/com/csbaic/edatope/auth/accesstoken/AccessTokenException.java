package com.csbaic.edatope.auth.accesstoken;

public class AccessTokenException extends RuntimeException {

    public AccessTokenException() {
    }

    public AccessTokenException(String message) {
        super(message);
    }

    public AccessTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccessTokenException(Throwable cause) {
        super(cause);
    }

    public AccessTokenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
