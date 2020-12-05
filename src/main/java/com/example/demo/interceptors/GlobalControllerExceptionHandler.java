package com.example.demo.interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    /*@ExceptionHandler(value = {DeniedPermissionException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String deniedPermissionException(DeniedPermissionException ex) {
        return "Denied permission";
    }*/

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity constraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.status(400).build();
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity internalServerError(Exception ex) {
        return ResponseEntity.status(500).build();
    }
}
