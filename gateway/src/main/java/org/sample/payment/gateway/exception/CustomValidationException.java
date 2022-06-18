package org.sample.payment.gateway.exception;

public class CustomValidationException extends RuntimeException{
    public CustomValidationException() {
    }

    public CustomValidationException(String message) {
        super(message);
    }

    public CustomValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
