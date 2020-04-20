package com.zkejid.constructor.core.api.v1;

/**
 * Exception which is happened during construction of application from modules.
 */
public class ConstructionException extends RuntimeException {

    public ConstructionException() {
    }

    public ConstructionException(String message) {
        super(message);
    }

    public ConstructionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConstructionException(Throwable cause) {
        super(cause);
    }

    public ConstructionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
