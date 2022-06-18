package org.sample.payment.gateway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomValidationException.class)
    public ResponseEntity<Object> handleCustomException(CustomException exception){
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;

        CustomException customException = new CustomException(exception.getMessage(), exception.getThrowable(), badRequest);

        return new ResponseEntity<>(customException, badRequest);
    }
}
