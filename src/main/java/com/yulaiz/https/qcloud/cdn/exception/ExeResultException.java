package com.yulaiz.https.qcloud.cdn.exception;

/**
 * Created by YuLai on 2018/3/7.
 */
public class ExeResultException extends RuntimeException {

    public ExeResultException() {
    }

    public ExeResultException(String message) {
        super(message);
    }

    public ExeResultException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExeResultException(Throwable cause) {
        super(cause);
    }
}
