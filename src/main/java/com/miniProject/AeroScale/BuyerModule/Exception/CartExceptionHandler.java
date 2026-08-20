package com.miniProject.AeroScale.BuyerModule.Exception;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CartExceptionHandler {

    @ExceptionHandler(CartItemNotFoundException.class)
    public ResponseEntity<?> cartItemNotFoundException(CartItemNotFoundException cartItemNotFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(cartItemNotFoundException.getMessage());
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<?> insufficientStockException(InsufficientStockException insufficientStockException) {
        return ResponseEntity.status(HttpStatus.INSUFFICIENT_STORAGE.value()).body(insufficientStockException.getMessage());
    }

    @ExceptionHandler(RequiredThingsNotFoundException.class)
    public ResponseEntity<?> requiredThingsNotFound(RequiredThingsNotFoundException requiredThingsNotFoundException) {
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED.value()).body(requiredThingsNotFoundException.getMessage());
    }


}
