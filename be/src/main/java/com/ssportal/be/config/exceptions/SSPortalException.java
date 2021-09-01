package com.ssportal.be.config.exceptions;

public class SSPortalException extends Exception {

    public SSPortalException() {
        super();
    }

    public SSPortalException(String message) {
        super(message);
    }

    public SSPortalException(String message, Throwable cause) {
        super(message, cause);
    }

    public SSPortalException(Throwable cause) {
        super(cause);
    }

    protected SSPortalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
