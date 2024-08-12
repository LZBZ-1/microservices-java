package com.lzbz.product.exception;

import com.lzbz.product.common.StandardizedApiExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleUnknownException(Exception ex){
        StandardizedApiExceptionResponse standardizedApiExceptionResponse = new StandardizedApiExceptionResponse
        (
            "Tecnico",
            "Input Output error",
            "1024",
            ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(standardizedApiExceptionResponse);
    }
}
