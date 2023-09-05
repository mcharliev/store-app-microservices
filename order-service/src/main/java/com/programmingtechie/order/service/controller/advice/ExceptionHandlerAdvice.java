package com.programmingtechie.order.service.controller.advice;


import com.programmingtechie.order.service.exception.ExceptionDetails;
import com.programmingtechie.order.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDetails>  UserNotFoundExceptionHandler() {
        ExceptionDetails exceptionDetails = new ExceptionDetails();
        exceptionDetails.setMessage(HttpStatus.NOT_FOUND.toString());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionDetails);
    }
}
